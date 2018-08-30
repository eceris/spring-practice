package kr.co.eceris.spring.test.post;

import kr.co.eceris.spring.test.ApplicationTests;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PostTest extends ApplicationTests {

    @Autowired
    private PostService service;

    @Test
    public void 생성_5개_테스트() {
        //given
        service.create();
        service.create();
        service.create();
        //when
        List<Post> list = service.list();

        //then
        Assert.assertEquals(list.size(), 3);
    }
}
