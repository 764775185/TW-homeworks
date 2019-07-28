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

    @Autowired
    private CurrentUserInfoService currentUserInfoService;

    public PostResponse<Iterable<Posts>> getAllPosts(){
        return new PostResponse<>(200,"文章数据获取成功！", postRepository.findAllOderByDesc());
    }

    public PostResponse<Posts> newPost(Posts posts) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String strStartTime = sdf.format(new Date());
        Users users = currentUserInfoService.getUserInfo();
        Posts p = new Posts(posts.getTitle(), posts.getContent(),strStartTime, users);
        postRepository.save(p);
        return new PostResponse<>(200,"文章发表成功！",p);
    }

    public PostResponse<Posts> findPost(Integer id) {
        Optional<Posts> p = postRepository.findById(id);
        if (!p.isPresent()){
            throw new BasePostException("该文章不存在！");
        }
        postRepository.save(p.get());
        return new PostResponse<>(200,"文章查找成功！",p.get());
    }


    public PostResponse<Posts> updatePost(Posts posts) {
        Optional<Posts> p = postRepository.findById(posts.getId());
        if (!p.isPresent()) {
            throw new BasePostException("该文章不存在！");
        } else {
            Users u = currentUserInfoService.getUserInfo();
            if (u.getId().equals(p.get().getUsers().getId()) || u.getRole().equals("ROLE_ADMIN")) {
                p.get().setTitle(posts.getTitle());
                p.get().setContent(posts.getContent());
                postRepository.save(p.get());
                return new PostResponse<>(200, "文章更新成功！", p.get());
            }
            throw new AuthorizationException("您没有修改此文章的权限！");
        }
    }

    public PostResponse<Posts> deletePost(Integer id) {
        Optional<Posts> p = postRepository.findById(id);
        if (!p.isPresent()){
            throw new BasePostException("该文章不存在！");
        }
        else {
            Users u = currentUserInfoService.getUserInfo();
            if (u.getId().equals(p.get().getUsers().getId()) || u.getRole().equals("ROLE_ADMIN")) {
                postRepository.deleteById(id);
                return new PostResponse<>(200, "文章删除成功！", p.get());
            }
            throw new AuthorizationException("您没有删除此文章的权限！");
        }
    }

}