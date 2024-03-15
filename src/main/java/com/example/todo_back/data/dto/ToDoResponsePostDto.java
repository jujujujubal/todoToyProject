package com.example.todo_back.data.dto;
import com.example.todo_back.data.constant.ColorList;
import com.example.todo_back.data.entity.ContentEntity;
import com.example.todo_back.data.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToDoResponsePostDto {
    private String postId;
    private String userNickname;
    private String title;
    private List<ToDoResponseContentDto> content;
    private LocalDateTime createTime;
    private ColorList color;

//    public PostEntity toEntity(){
//        PostEntity postEntity = new PostEntity();
//        postEntity.setPostId(this.postId);
//        postEntity.setUserId(this.userId);
//        postEntity.setUserNickname(this.userNickname);
//        postEntity.setTitle(this.title);
//        postEntity.setCreateTime(this.createTime);
//        postEntity.setColor(this.color);
//
//        List<ContentEntity> contentEntityList = new ArrayList<>();
//
//        for (ToDoResponseContentDto toDoResponseContentDto : this.content) {
//            contentEntityList.add(toDoResponseContentDto.to)
//        }
//    }
}
