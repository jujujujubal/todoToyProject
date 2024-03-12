package com.example.todo_back.data.dto;
import com.example.todo_back.data.constant.ToDoEntityStatus;
import com.example.todo_back.data.entity.ContentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToDoRequestContentDto {
    private String content;
    private ToDoEntityStatus isComplete;
    private String indexNum;

    public ContentEntity toEntity() {
        ContentEntity contentEntity = new ContentEntity();
        contentEntity.setId(UUID.randomUUID().toString());
        contentEntity.setContent(this.getContent());
        contentEntity.setIsComplete(this.getIsComplete());
        contentEntity.setIndexNum(this.getIndexNum());

        return contentEntity;
    }
}
