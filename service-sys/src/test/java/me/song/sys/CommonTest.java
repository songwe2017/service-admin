package me.song.sys;

import io.lettuce.core.dynamic.support.ReflectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.DefaultParameterNameDiscoverer;

import java.lang.reflect.Method;

/**
 * @author Songwe
 * @since 2023/6/2 14:35
 */
public class CommonTest {
    @Test
    public void testParameterNameDiscoverer() {
        String data = "20230408";

        data = data.substring(0,4) + "-" + data.substring(4,6) + data.substring(6,8);

        System.out.println(data);
    }

    public class TestController {
        public String getParamName(String userName, Integer age) {
            System.out.println(userName);
            return userName;
        }
    }

}
