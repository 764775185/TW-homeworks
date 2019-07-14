package com.thoughtworks.homework.service;

import com.thoughtworks.homework.dto.PostResponse;
import com.thoughtworks.homework.entity.Posts;
import com.thoughtworks.homework.entity.Users;
import com.thoughtworks.homework.exception.AuthorizationException;
import com.thoughtworks.homework.exception.BasePostException;
import com.thoughtworks.homework.repository.PostRepository;
import com.thoughtworks.homework.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private Users getUserInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Users> user = userRepository.findUserByEmail((String) principal);
        return user.get();
    }

    private PostResponse<Posts> generatePostRes(int code, String message, Posts data){
        PostResponse<Posts> postPostResponse = new PostResponse<>();
        postPostResponse.setCode(code);
        postPostResponse.setMessage(message);
        postPostResponse.setData(data);
        return postPostResponse;
    }

    public PostResponse<Iterable<Posts>> getAllPosts(){
        PostResponse<Iterable<Posts>> postResponse= new PostResponse<>();
        postResponse.setCode(200);
        postResponse.setMessage("文章数据获取成功！");
        postResponse.setData(postRepository.findAllOderByDesc());
        return postResponse;
    }

    public PostResponse<Posts> newPost(Posts posts) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String strStartTime = sdf.format(new Date());
        Users users = getUserInfo();
        Posts p = new Posts(posts.getTitle(), posts.getContent(),strStartTime, users);
        postRepository.save(p);
        return generatePostRes(200,"文章发表成功！",p);
    }

    public PostResponse<Posts> findPost(Integer id) {
        Optional<Posts> p = postRepository.findById(id);
        if (!p.isPresent()){
            throw new BasePostException("该文章不存在！");
        }
        postRepository.save(p.get());
        return generatePostRes(200,"文章查找成功！",p.get());
    }

    public PostResponse<Posts> updatePost(Posts posts) {
        Users u = getUserInfo();
        if (u.getId().equals(posts.getId()) || u.getRole().equals("ROLE_ADMIN")) {
            Optional<Posts> p = postRepository.findById(posts.getId());
            if (!p.isPresent()) {
                throw new BasePostException("该文章不存在！");
            }
            p.get().setTitle(posts.getTitle());
            p.get().setContent(posts.getContent());
            postRepository.save(p.get());
            return generatePostRes(200, "文章更新成功！", p.get());
        }
        throw new AuthorizationException("您没有修改此文章的权限！");
    }

    public PostResponse<Posts> deletePost(Integer id) {
        Users u = getUserInfo();
        if (u.getId().equals(id) || u.getRole().equals("ROLE_ADMIN")){
            Optional<Posts> p = postRepository.findById(id);
            if (!p.isPresent()){
                throw new BasePostException("该文章不存在！");
            }
            postRepository.deleteById(id);
            return generatePostRes(200,"文章删除成功！",p.get());
        }
        throw new AuthorizationException("您没有删除此文章的权限！");
    }

}