package com.example.todo_back.data.entity;
import com.example.todo_back.data.constant.ToDoEntityStatus;
import com.example.todo_back.data.dto.ToDoResponseContentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "todoContent")
public class ContentEntity {
    @Id
    @Column(name = "id")
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @ManyToOne
    @JoinColumn(name = "post_postId")
    private PostEntity post;

    @Column(nullable = false)
    private String content;

    @Column(name = "iscomplete")
    @Enumerated(EnumType.STRING)
    private ToDoEntityStatus isComplete;

//    이거 대신 @GeneratedValue(strategy = GenerationType.IDENTITY) 을 쓰면 어떨까?
    @Column(name = "indexNum")
    private String indexNum;

    public void changeComplete(){
        if (this.isComplete.equals(ToDoEntityStatus.COMPLETE)){
            this.isComplete = ToDoEntityStatus.UNCOMPLETED;
        } else {
            this.isComplete = ToDoEntityStatus.COMPLETE;
        }
    }
    public ToDoResponseContentDto toDto() {
        ToDoResponseContentDto toDoResponseContentDto = new ToDoResponseContentDto();
        toDoResponseContentDto.setId(this.id);
        toDoResponseContentDto.setContent(this.content);
        toDoResponseContentDto.setIsComplete(this.isComplete);
        return toDoResponseContentDto;
    }

}
