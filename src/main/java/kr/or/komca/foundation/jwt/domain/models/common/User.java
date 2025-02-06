//package kr.or.komca.foundation.jwt.domain.models.common;
//
//import kr.or.komca.foundation.jwt.domain.enums.RoleType;
//import lombok.*;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 사용자 도메인 엔티티
// */
//@Getter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//@ToString(exclude = "password")
//public class User {
//    /** 사용자 번호 */
//    private Long userNo;
//
//    /** 사용자 아이디 */
//    private String username;
//
//    /** 비밀번호 (암호화됨) */
//    private String password;
//
//    /** 이름 */
//    private String name;
//
//    /** 이메일 */
//    private String email;
//
//    /** 계정 활성화 여부 */
//    private boolean enabled;
//
//    /** 역할 목록 */
//    @Builder.Default
//    private List<RoleType> roles = new ArrayList<>();
//
//    /** 등록일시 */
//    private LocalDateTime regDate;
//
//    /** 수정일시 */
//    private LocalDateTime modDate;
//
//    /** 삭제 여부 */
//    private String delYn;
//}