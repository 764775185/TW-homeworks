package com.thoughtworks.homework.service;

import com.thoughtworks.homework.dto.PostResponse;
import com.thoughtworks.homework.dto.UserResponse;
import com.thoughtworks.homework.entity.Post;
import com.thoughtworks.homework.entity.User;
import com.thoughtworks.homework.exception.BasePostException;
import com.thoughtworks.homework.repository.PostRespository;
import com.thoughtworks.homework.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRespository postRespository;

    private User getUserInfo(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepository.findUserByEmail((String) principal);
        return user.get();
    }

    public PostResponse<Iterable<Post>> getAllPosts(){
        PostResponse<Iterable<Post>> postResponse = new PostResponse<>();
        postResponse.setCode(200);
        postResponse.setMessage("所有文章数据获取成功！");
        postResponse.setData(postRespository.findAll());
        return postResponse;
    }

    public PostResponse<Post> addNewPost(Post post){
        User user = getUserInfo();
        Post p = new Post(post.getTitle(),post.getContent(),user);
        postRespository.save(p);
        PostResponse<Post> postResponse = new PostResponse<>();
        postResponse.setCode(200);
        postResponse.setMessage("文章发表成功！");
        postResponse.setData(p);
        return postResponse;
    }

    public PostResponse<Post> findPostById(Integer id) {
        Optional<Post> p = postRespository.findById(id);
        if (!p.isPresent()){
            throw new BasePostException("该文章不存在！");
        }
        postRespository.save(p.get());
        PostResponse<Post> postPostResponse = new PostResponse<>();
        postPostResponse.setCode(200);
        postPostResponse.setMessage("文章查找成功！");
        postPostResponse.setData(p.get());
        return postPostResponse;
    }

    public PostResponse<Post> updatePost(Post post) {
        Optional<Post> p = postRespository.findById(post.getId());
        if (!p.isPresent()){
            throw new BasePostException("该文章不存在！");
        }
        p.get().setTitle(post.getTitle());
        p.get().setContent(post.getContent());
        postRespository.save(p.get());
        PostResponse<Post> postPostResponse = new PostResponse<>();
        postPostResponse.setCode(200);
        postPostResponse.setMessage("文章更新成功！");
        postPostResponse.setData(p.get());
        return postPostResponse;
    }

    public PostResponse<Post> deletePostById(Integer id) {
        Optional<Post> p = postRespository.findById(id);
        if (!p.isPresent()){
            throw new BasePostException("该文章不存在！");
        }
        postRespository.deleteById(id);
        PostResponse<Post> postPostResponse = new PostResponse<>();
        postPostResponse.setCode(200);
        postPostResponse.setMessage("文章删除成功！");
        postPostResponse.setData(p.get());
        return postPostResponse;
    }
}
