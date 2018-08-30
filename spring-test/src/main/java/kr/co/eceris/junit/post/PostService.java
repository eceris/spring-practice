package kr.co.eceris.junit.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository repository;

    Post get(Long id) {
        return repository
                .findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    Post create() {
        return repository.save(new Post("title", "content"));
    }

    List<Post> list() {
        return StreamSupport
                .stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

}
