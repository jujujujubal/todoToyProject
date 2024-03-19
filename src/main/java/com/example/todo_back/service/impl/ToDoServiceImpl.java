package com.example.todo_back.service.impl;

import com.example.todo_back.data.dto.ToDoRequestContentDto;
import com.example.todo_back.data.dto.ToDoRequestPostDto;
import com.example.todo_back.data.dto.ToDoResponseContentDto;
import com.example.todo_back.data.dto.ToDoResponsePostDto;
import com.example.todo_back.data.entity.ContentEntity;
import com.example.todo_back.data.entity.MemberEntity;
import com.example.todo_back.data.entity.PostEntity;
import com.example.todo_back.data.repository.ContentRepository;
import com.example.todo_back.data.repository.MemberRepository;
import com.example.todo_back.data.repository.PostRepository;
import com.example.todo_back.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import org.hibernate.Hibernate;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Comparator;

@Transactional
@Service
public class ToDoServiceImpl implements ToDoService {

    private final PostRepository postRepository;
    private final ContentRepository contentRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public ToDoServiceImpl(PostRepository postRepository, ContentRepository contentRepository, MemberRepository memberRepository){
        this.postRepository = postRepository;
        this.contentRepository = contentRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public List<ToDoResponsePostDto> getToDo(){
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
    public ToDoResponsePostDto saveToDo(ToDoRequestPostDto toDoRequestPostDto, String userPersonalId){
        System.out.println("@@@@@@@@@@@userPersonalId는 " + userPersonalId);

        MemberEntity memberEntity = memberRepository.findByPersonalId(userPersonalId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));

        PostEntity postEntity = new PostEntity();
        postEntity.setPostId(UUID.randomUUID().toString());
        postEntity.setUserId(memberEntity.getPersonalId());
        postEntity.setUserNickname(memberEntity.getNickname());
        postEntity.setTitle(toDoRequestPostDto.getTitle());
        postEntity.setColor(memberEntity.getColor());

        PostEntity savedPost = postRepository.save(postEntity);
        List<ToDoResponseContentDto> toDoResponseContentDtoList = new ArrayList<>();
        for (ToDoRequestContentDto toDoRequestContentDto : toDoRequestPostDto.getContent()) {
            ContentEntity contentEntity = toDoRequestContentDto.toEntity();
            contentEntity.setPost(savedPost);
            ContentEntity saveContent = contentRepository.save(contentEntity);
            toDoResponseContentDtoList.add(saveContent.toDto());
        }

        ToDoResponsePostDto toDoResponsePostDto = new ToDoResponsePostDto();
        toDoResponsePostDto.setPostId(savedPost.getPostId());
        toDoResponsePostDto.setUserNickname(savedPost.getUserNickname());
        toDoResponsePostDto.setTitle(savedPost.getTitle());
        toDoResponsePostDto.setColor(savedPost.getColor());
        toDoResponsePostDto.setCreateTime(savedPost.getCreateTime());
        toDoResponsePostDto.setContent(toDoResponseContentDtoList);

        return toDoResponsePostDto;
    }

    @Override
    public ToDoResponsePostDto updateToDo(ToDoRequestPostDto toDoRequestPostDto, String postId){
        Optional<PostEntity> optionalPostEntity = postRepository.findById(postId);
        if (optionalPostEntity.isPresent()){
            PostEntity postEntity = optionalPostEntity.get();
            PostEntity new_postEntity = new PostEntity();
            new_postEntity.setPostId(postEntity.getPostId());
            new_postEntity.setUserId(postEntity.getUserId());
            new_postEntity.setUserNickname(postEntity.getUserNickname());
            new_postEntity.setTitle(toDoRequestPostDto.getTitle());
            new_postEntity.setCreateTime(postEntity.getCreateTime());
            new_postEntity.setColor(postEntity.getColor());

            List<ContentEntity> contentEntityList = new ArrayList<>();
            for (ToDoRequestContentDto toDoRequestContentDto : toDoRequestPostDto.getContent()) {
                ContentEntity contentEntity = toDoRequestContentDto.toEntity();
                contentEntity.setPost(postEntity);
                contentEntityList.add(contentEntity);
            }
            new_postEntity.setContents(contentEntityList);

            PostEntity savedPostEntity = postRepository.save(new_postEntity);

            return savedPostEntity.toDto();
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
    public void deleteToDo(String postId){
        postRepository.deleteByPostId(postId);
    }
}
