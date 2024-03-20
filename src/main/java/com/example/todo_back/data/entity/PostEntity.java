package com.example.todo_back.data.entity;
import com.example.todo_back.data.constant.ColorList;
import com.example.todo_back.data.dto.ToDoResponseContentDto;
import com.example.todo_back.data.dto.ToDoResponsePostDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "todoPost")
public class PostEntity {
    @Id
    @Column(name = "postid")
    private String postId;

    @Column(name = "userid")
    private String userId;

    @Column(name = "usernickname")
    private String userNickname;

    @Column(nullable = false, length = 50)
    private String title;

    //이거 대신 BaseTimeEntity 을 상속받으면 어떨까?
    @CreationTimestamp
    @Column(name = "createtime")
    private LocalDateTime createTime;

    @Column(name = "color")
    private ColorList color;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<ContentEntity> contents;


    public ToDoResponsePostDto toDto() {
        ToDoResponsePostDto toDoResponsePostDto = new ToDoResponsePostDto();
        toDoResponsePostDto.setPostId(this.postId);
        toDoResponsePostDto.setUserNickname(this.userNickname);
        toDoResponsePostDto.setTitle(this.title);
        toDoResponsePostDto.setCreateTime(this.createTime);
        toDoResponsePostDto.setColor(this.color.toString());
        List<ToDoResponseContentDto> toDoResponseContentDto = new ArrayList<>();

        for (ContentEntity contentEntity : this.contents) {
            toDoResponseContentDto.add(contentEntity.toDto());
        }

        toDoResponsePostDto.setContent(toDoResponseContentDto);
        return toDoResponsePostDto;
    }

}
