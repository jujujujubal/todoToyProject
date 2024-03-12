package com.example.todo_back.service.impl;

import com.example.todo_back.data.dto.ToDoRequestContentDto;
import com.example.todo_back.data.dto.ToDoRequestPostDto;
import com.example.todo_back.data.dto.ToDoResponseContentDto;
import com.example.todo_back.data.dto.ToDoResponsePostDto;
import com.example.todo_back.data.entity.ContentEntity;
import com.example.todo_back.data.entity.PostEntity;
import com.example.todo_back.data.repository.ContentRepository;
import com.example.todo_back.data.repository.PostRepository;
import com.example.todo_back.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Comparator;

@Service
public class ToDoServiceImpl implements ToDoService {

    private final PostRepository postRepository;
    private final ContentRepository contentRepository;

    @Autowired
    public ToDoServiceImpl(PostRepository postRepository, ContentRepository contentRepository){
        this.postRepository = postRepository;
        this.contentRepository = contentRepository;
    }

    @Override
    public List<ToDoResponsePostDto> getToDo(){//수정하기(너무 길다)
        List<PostEntity> postEntityList = postRepository.findAll(Sort.by(Sort.Direction.ASC, "createTime"));
        List<ToDoResponsePostDto> toDoResponsePostDtoList = new ArrayList<>();

        for (PostEntity postEntity : postEntityList) {
            ToDoResponsePostDto toDoResponsePostDto = postEntity.toDto();
            List<ToDoResponseContentDto> toDoResponseContentDtoList = new ArrayList<>();

            List<ContentEntity> sortedContents = postEntity.getContents().stream()
                    .sorted(Comparator.comparing(ContentEntity::getIndexNum))
                    .toList();

            for (ContentEntity contentEntity : sortedContents) {
                toDoResponseContentDtoList.add(contentEntity.toDto());
            }
            toDoResponsePostDto.setContent(toDoResponseContentDtoList);
            toDoResponsePostDtoList.add(toDoResponsePostDto);
        }
        return toDoResponsePostDtoList;
    }

    @Override
    @Transactional
    public ToDoResponsePostDto saveToDo(ToDoRequestPostDto toDoRequestPostDto){
        PostEntity postEntity = new PostEntity();
        postEntity.setPostId(UUID.randomUUID().toString());
        postEntity.setUserId(toDoRequestPostDto.getUserId());
        postEntity.setTitle(toDoRequestPostDto.getTitle());
        postEntity.setColor(toDoRequestPostDto.getColor());

        PostEntity savedPost = postRepository.save(postEntity);
        List<ToDoResponseContentDto> toDoResponseContentDtoList = new ArrayList<>();
        for (ToDoRequestContentDto toDoRequestContentDto : toDoRequestPostDto.getContent()) {
            ContentEntity contentEntity = new ContentEntity();
            contentEntity.setPost(savedPost);
            contentEntity.setContent(toDoRequestContentDto.getContent());
            contentEntity.setIsComplete(toDoRequestContentDto.getIsComplete());
            contentEntity.setIndexNum(toDoRequestContentDto.getIndexNum());
            contentEntity.setId(UUID.randomUUID().toString());
            ContentEntity saveContent = contentRepository.save(contentEntity);
            toDoResponseContentDtoList.add(saveContent.toDto());
        }

        ToDoResponsePostDto toDoResponsePostDto = new ToDoResponsePostDto();
        toDoResponsePostDto.setPostId(savedPost.getPostId());
        toDoResponsePostDto.setUserId(savedPost.getUserId());
        toDoResponsePostDto.setTitle(savedPost.getTitle());
        toDoResponsePostDto.setColor(savedPost.getColor());
        toDoResponsePostDto.setCreateTime(savedPost.getCreateTime());
        toDoResponsePostDto.setContent(toDoResponseContentDtoList);

        return toDoResponsePostDto;
    }

    @Override
    @Transactional
    public ToDoResponsePostDto updateToDo(ToDoRequestPostDto toDoRequestPostDto, String postId){
        Optional<PostEntity> optionalPostEntity = postRepository.findById(postId);
        if (optionalPostEntity.isPresent()){
            PostEntity postEntity = optionalPostEntity.get();
            LocalDateTime localDateTime = postEntity.getCreateTime();
            deleteToDo(postId);
            return saveToDo(toDoRequestPostDto);
        } else {
            throw new RuntimeException("입력한 POST ID와 일치하는 POST가 존재하지 않습니다. Post with ID " + postId + " not found");
        }
    }

    @Override
    public ToDoResponseContentDto changeComplete(String id){
        Optional<ContentEntity> optionalContentEntity = contentRepository.findById(id);
        if (optionalContentEntity.isPresent()){
            ContentEntity contentEntity = optionalContentEntity.get();
            contentEntity.changeComplete();
            contentRepository.save(contentEntity);
            return contentEntity.toDto();
        } else {
            throw new RuntimeException("입력한 ID와 일치하는 투두가 존재하지 않습니다. Post with ID " + id + " not found");
        }
    }


    @Override
    @Transactional
    public void deleteToDo(String postId){
        postRepository.deleteByPostId(postId);
    }
}
