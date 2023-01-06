package org.jamie.mybatis.handler.impl;

import org.jamie.mybatis.handler.TokenHandler;
import org.jamie.mybatis.parser.ParameterMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Frank D. Martinez
 */
public class ParameterMappingTokenHandler implements TokenHandler {

    private List<ParameterMapping> parameterMappings = new ArrayList<>();

    @Override
    public String handleToken(String content) {
      parameterMappings.add(new ParameterMapping(content));
      return "?";
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }
}