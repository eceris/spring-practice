package kr.co.eceris.spring.test.post;

import kr.co.eceris.spring.test.ApplicationTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PostTest extends ApplicationTests {

    @Autowired
    private PostService service;

    @Before
    public void clean() {
        service.delete();
    }

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

    @Test(expected = IllegalArgumentException.class)
    public void 생성하지_않은_포스트를_찾는다() {
        //given
        //when
        Post post = service.get(300l);
        System.out.println(post);

        //then
        // expect IllegalArgumentException
    }
}
