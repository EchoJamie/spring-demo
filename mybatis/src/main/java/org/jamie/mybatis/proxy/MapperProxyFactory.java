package org.jamie.mybatis.proxy;

import org.jamie.mybatis.annotation.Param;
import org.jamie.mybatis.annotation.Select;
import org.jamie.mybatis.constant.MysqlConfig;
import org.jamie.mybatis.handler.TypeHandler;
import org.jamie.mybatis.handler.impl.IntegerTypeHandler;
import org.jamie.mybatis.handler.impl.StringTypeHandler;
import org.jamie.mybatis.parser.GenericTokenParser;
import org.jamie.mybatis.parser.ParameterMapping;
import org.jamie.mybatis.handler.impl.ParameterMappingTokenHandler;

import java.lang.reflect.*;
import java.sql.*;
import java.util.*;

/**
 * @author jamie
 * @version 1.0.0
 * @description 映射代理工厂
 * @date 2023/01/02 05:50
 */
public class MapperProxyFactory {

    private static Map<Class, TypeHandler> typeHandlerMap = new HashMap<>();

    static {
        typeHandlerMap.put(String.class, new StringTypeHandler());
        typeHandlerMap.put(Integer.class, new IntegerTypeHandler());

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static <T> T getMapper(Class mapper) {
        Object proxyInstance = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{mapper}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 解析SQL
                Connection connection = getConnection();
                Select select = method.getAnnotation(Select.class);
                String sql = select.value();
                System.out.println(sql);

                Map<String, Object> paramValueMap = new HashMap<>();
                Parameter[] parameters = method.getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    Parameter parameter = parameters[i];
                    String name = parameter.getAnnotation(Param.class).value();
                    paramValueMap.put(parameter.getName(), args[i]);
                    paramValueMap.put(name, args[i]);
                }

                ParameterMappingTokenHandler tokenHandler = new ParameterMappingTokenHandler();
                GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", tokenHandler);
                String parseSql = genericTokenParser.parse(sql);
                List<ParameterMapping> parameterMappings = tokenHandler.getParameterMappings();

                PreparedStatement preparedStatement = connection.prepareStatement(parseSql);
                for (int i = 0; i < parameterMappings.size(); i++) {
                    String property = parameterMappings.get(i).getProperty();
                    Object value = paramValueMap.get(property);
                    Class<?> type = value.getClass();

                    typeHandlerMap.get(type).setParameter(preparedStatement, i + 1, value);
                }

                // 执行SQL
                preparedStatement.execute();
                ResultSet resultSet = preparedStatement.getResultSet();
                // 封装对象
                Object result = null;
                List<Object> resultList = new ArrayList<>();

                // 获取返回值类型
                Class resultType = null;
                Type genericReturnType = method.getGenericReturnType();
                if (genericReturnType instanceof Class) {
                    // 不是泛型
                    resultType = (Class) genericReturnType;
                } else if (genericReturnType instanceof ParameterizedType) {
                    // 是泛型
                    Type[] actualTypeArguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();
                    resultType = (Class) actualTypeArguments[0];
                }

                // 获取结果集 字段
                ResultSetMetaData metaData = resultSet.getMetaData();
                List<String> columnList = new ArrayList<>();
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    columnList.add(metaData.getColumnName(i + 1));
                }

                Map<String, Method> setterMethodMap = new HashMap<>();
                for (Method declaredMethod : resultType.getDeclaredMethods()) {
                    if (declaredMethod.getName().startsWith("set")) {
                        String propertyName = declaredMethod.getName().substring(3);
                        propertyName= propertyName.substring(0, 1).toLowerCase(Locale.ROOT) + propertyName.substring(1);
                        setterMethodMap.put(propertyName, declaredMethod);
                    }
                }

                while (resultSet.next()) {
                    Object instance = resultType.newInstance();
                    for (String column : columnList) {
                        Method setterMethod = setterMethodMap.get(column);
                        TypeHandler typeHandler = typeHandlerMap.get(setterMethod.getParameterTypes()[0]);
                        setterMethod.invoke(instance, typeHandler.getResult(resultSet, column));
                    }
                    resultList.add(instance);
                }

                if (method.getReturnType().equals(List.class)) {
                    result = resultList;
                } else {
                    result = resultList.get(0);
                }

                return result;
            }

            private Connection getConnection() throws SQLException {
                return DriverManager.getConnection(MysqlConfig.URL, MysqlConfig.USERNAME, MysqlConfig.PASSWORD);
            }

            private Object jdbcQuery() throws SQLException {
                // 创建连接
                Connection connection = DriverManager.getConnection(MysqlConfig.URL, MysqlConfig.USERNAME, MysqlConfig.PASSWORD);
                // 编写SQL
                PreparedStatement preparedStatement = connection.prepareStatement("select * from user where name = ? and age = ?");
                // 参数赋值
                preparedStatement.setString(1, "Jamie");
                preparedStatement.setInt(2, 18);
                // 执行SQL
                preparedStatement.execute();
                // 封装结果
                List<Map<String, Object>> list = new ArrayList<>();
                ResultSet resultSet = preparedStatement.getResultSet();
                while (resultSet.next()) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id", resultSet.getString("id"));
                    map.put("name", resultSet.getString("name"));
                    map.put("age", resultSet.getInt("age"));
                    list.add(map);
                }
                System.out.println("factory: " + list);
                resultSet.close();
                preparedStatement.close();
                connection.close();
                return list;
            }
        });

        return (T) proxyInstance;
    }
}
