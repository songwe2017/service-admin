package me.song.sys;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Songwe
 * @since 2023/3/27 15:37
 */
@SpringBootTest(classes = SYSApplication.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public abstract class AbstractBaseTest {
}
