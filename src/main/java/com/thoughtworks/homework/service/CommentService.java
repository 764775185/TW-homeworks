package com.thoughtworks.homework.service;

import com.thoughtworks.homework.dto.CommentResponse;
import com.thoughtworks.homework.entity.Comments;
import com.thoughtworks.homework.entity.Posts;
import com.thoughtworks.homework.entity.Users;
import com.thoughtworks.homework.exception.AuthorizationException;
import com.thoughtworks.homework.exception.BaseCommentException;
import com.thoughtworks.homework.exception.BasePostException;
import com.thoughtworks.homework.repository.CommentRepository;
import com.thoughtworks.homework.repository.PostRepository;
import com.thoughtworks.homework.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CurrentUserInfoService currentUserInfoService;

    private Posts getPostInfo(int post_id) {
        Optional<Posts> posts = postRepository.findById(post_id);
        return posts.get();
    }

    public CommentResponse<Iterable<Comments>> getAllComments(){

        return new CommentResponse<>(200,"评论数据获取成功！",commentRepository.findAllOderByDesc());
    }

    public CommentResponse<Comments> newComment(int post_id,Comments comments) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String strStartTime = sdf.format(new Date());
        Users users = currentUserInfoService.getUserInfo();
        Posts posts = getPostInfo(post_id);
        Comments p = new Comments(comments.getTitle(), comments.getContent(),strStartTime, users, posts);
        commentRepository.save(p);
        return new CommentResponse<>(200,"评论发表成功！",p);
    }

    public CommentResponse<Comments> findComment(Integer id) {
        Optional<Comments> p = commentRepository.findById(id);
        if (!p.isPresent()){
            throw new BaseCommentException("该评论不存在！");
        }
        return new CommentResponse<>(200,"评论查找成功！",p.get());
    }

    public CommentResponse<Comments> updateComment(Comments comments) {
        Optional<Comments> c = commentRepository.findById(comments.getId());
        if (!c.isPresent()) {
                throw new BaseCommentException("该评论不存在！");
        }
        else {
            Users u = currentUserInfoService.getUserInfo();
            if (u.getId().equals(c.get().getUsers().getId()) || u.getId().equals(c.get().getPosts().getId()) || u.getRole().equals("ROLE_ADMIN")) {
                c.get().setTitle(comments.getTitle());
                c.get().setContent(comments.getContent());
                commentRepository.save(c.get());
                return new CommentResponse<>(200, "评论更新成功！", c.get());
            }
            throw new AuthorizationException("您没有更新此评论的权限！");
        }
    }

    public CommentResponse<Comments> deleteComment (Integer id) {
        Optional<Comments> c = commentRepository.findById(id);
        if (!c.isPresent()) {
            throw new BaseCommentException("该评论不存在！");
        } else {
            Users u = currentUserInfoService.getUserInfo();
            if (u.getId().equals(c.get().getUsers().getId()) || u.getId().equals(c.get().getPosts().getId()) || u.getRole().equals("ROLE_ADMIN")) {
                commentRepository.deleteById(id);
                return new CommentResponse<>(200, "评论删除成功！", c.get());
            }
            throw new AuthorizationException("您没有删除此评论的权限！");
        }
    }
}
