package com.example.todo_back.data.dto;
import com.example.todo_back.data.constant.ColorList;
import com.example.todo_back.data.entity.ContentEntity;
import com.example.todo_back.data.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToDoRequestPostDto {
    private String title;
    private List<ToDoRequestContentDto> content;

//    public PostEntity toEntity() {
//        PostEntity postEntity = new PostEntity();
//        postEntity.setPostId(UUID.randomUUID().toString());
//        postEntity.setUserId(this.getUserId());
//        postEntity.setTitle(this.getTitle());
//        postEntity.setColor(this.getColor());
//
//        List<ContentEntity> contentEntityList = new ArrayList<>();
//
//        for (ToDoRequestContentDto toDoRequestContentDto : this.content) {
//            contentEntityList.add(toDoRequestContentDto.toEntity());
//        }
//
//        postEntity.setContents(contentEntityList);
//
//        return postEntity;
//    }
}
