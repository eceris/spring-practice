package kr.co.eceris.spring.test.post;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService service;

    @GetMapping("/post/{id}")
    public ResponseEntity<Post> get(@PathVariable("id") Long id) {
        Post post = service.get(id);
        return ResponseEntity.ok(post);
    }

    @PostMapping("/post")
    public ResponseEntity<Post> create() {
        Post save = service.create();
        return ResponseEntity.ok(save);
    }
}
