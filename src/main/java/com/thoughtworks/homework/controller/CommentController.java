package com.thoughtworks.homework.controller;

import com.thoughtworks.homework.dto.CommentResponse;
import com.thoughtworks.homework.entity.Comments;
import com.thoughtworks.homework.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/api")
@Api(tags = "CommentController")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ApiOperation(value = "查找全部评论")
    @GetMapping(path = "/comments")
    @ResponseBody
    public CommentResponse<Iterable<Comments>> getAllComments(){
        return commentService.getAllComments();
    }
    @ApiOperation(value = "查找单条评论")
    @GetMapping(path = "/comment")
    @ResponseBody
    public CommentResponse<Comments> getComment(@RequestParam Integer id){
        return commentService.findComment(id);
    }

    @ApiOperation(value = "发表评论")
    @PostMapping(path = "/comment")
    @ResponseBody
    public CommentResponse<Comments> createComment(@RequestParam int post_id, @RequestBody Comments comments){
        return commentService.newComment(post_id,comments);
    }

    @ApiOperation(value = "更新评论",notes = "只有作者和管理员可以更新")
    @PutMapping(path = "/comment")
    @ResponseBody
    public CommentResponse<Comments> updatecomment(@RequestBody Comments comments){
        return commentService.updateComment(comments);
    }

    @ApiOperation(value = "删除评论",notes = "只有作者和管理员可以删除")
    @DeleteMapping(path = "/comment")
    @ResponseBody
    public CommentResponse<Comments> deletecomment(@RequestParam Integer id){
        return commentService.deleteComment(id);
    }
}
