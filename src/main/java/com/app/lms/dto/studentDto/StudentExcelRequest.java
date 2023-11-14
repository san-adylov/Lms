package com.app.lms.dto.studentDto;

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;
import lombok.Data;
import com.app.lms.enums.StudyFormat;

@Data
public class StudentExcelRequest {

    @ExcelRow
    private int indexRow;

    @ExcelCellName("First name")
    private String firstName;

    @ExcelCellName("Last name")
    private String lastName;

    @ExcelCellName("Phone number")
    private String phoneNumber;

    @ExcelCellName("Study format")
    private StudyFormat studyFormat;

    @ExcelCellName("Email")
    private String email;

}
