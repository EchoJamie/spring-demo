package org.jamie.spring.env;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/20 01:59
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyValue {

    private String name;

    private Object value;
}
