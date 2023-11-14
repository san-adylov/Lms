package com.app.lms.service;

import com.app.lms.dto.SimpleResponse;
import com.app.lms.dto.linkDto.LinkRequest;
import com.app.lms.dto.linkDto.LinkResponse;
import java.util.List;

public interface LinkService {

    SimpleResponse saveLink(Long lessonId, LinkRequest linkRequest);

    List<LinkResponse> getAllLinks(Long lessonId);

    SimpleResponse updateLink(Long linkId, LinkRequest linkRequest);

    SimpleResponse deleteLinkById(Long linkId);

}
