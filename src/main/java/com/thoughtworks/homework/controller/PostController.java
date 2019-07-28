package com.thoughtworks.homework.controller;

import com.thoughtworks.homework.dto.PostResponse;
import com.thoughtworks.homework.entity.Posts;
import com.thoughtworks.homework.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/api")
@Api(tags = "PostController")
public class PostController {

    @Autowired
    private PostService postService;

    @ApiOperation(value = "查找全部文章")
    @GetMapping(path = "/posts")
    @ResponseBody
    public PostResponse<Iterable<Posts>> getAllPosts(){
        return postService.getAllPosts();
    }

    @ApiOperation(value = "查找单篇文章")
    @GetMapping(path = "/post")
    @ResponseBody
    public PostResponse<Posts> getPost(@RequestParam Integer id){
        return postService.findPost(id);
    }

    @ApiOperation(value = "发表文章")
    @PostMapping(path = "/post")
    @ResponseBody
    public PostResponse<Posts> createPost(@RequestBody Posts posts){
        return postService.newPost(posts);
    }

    @ApiOperation(value = "更新文章",notes = "只有作者和管理员可以更新")
    @PutMapping(path = "/post")
    @ResponseBody
    public PostResponse<Posts> updatePost(@RequestBody Posts posts){
        return postService.updatePost(posts);
    }

    @ApiOperation(value = "删除文章",notes = "只有作者和管理员可以删除")
    @DeleteMapping(path = "/post")
    @ResponseBody
    public PostResponse<Posts> deletePost(@RequestParam Integer id){
        return postService.deletePost(id);
    }

}
