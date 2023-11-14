package com.app.lms.dto.groupDto;

import lombok.Builder;
import java.util.List;

@Builder
public record PaginationGroupResponse(
        List<GroupResponse>groupResponses,
        int size,
        int page
) {
}
