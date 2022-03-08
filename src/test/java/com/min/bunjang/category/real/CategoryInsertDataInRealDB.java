package com.min.bunjang.category.real;

import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import com.min.bunjang.category.repository.FirstProductCategoryRepository;
import com.min.bunjang.category.repository.SecondProductCategoryRepository;
import com.min.bunjang.category.repository.ThirdProductCategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class CategoryInsertDataInRealDB {
    @Autowired
    private FirstProductCategoryRepository firstProductCategoryRepository;

    @Autowired
    private SecondProductCategoryRepository secondProductCategoryRepository;

    @Autowired
    private ThirdProductCategoryRepository thirdProductCategoryRepository;


    @DisplayName("")
    @Test
    void create_category_in_realLocalDB() {
        List<FirstProductCategory> firstProductCategories = Arrays.asList(
                FirstProductCategory.createFirstProductCategory("여성의류"),
                FirstProductCategory.createFirstProductCategory("남성의류"),
                FirstProductCategory.createFirstProductCategory("신발"),
                FirstProductCategory.createFirstProductCategory("가방"),
                FirstProductCategory.createFirstProductCategory("시계/주얼리"),
                FirstProductCategory.createFirstProductCategory("패션 액세서리"),
                FirstProductCategory.createFirstProductCategory("디지털/가전"),
                FirstProductCategory.createFirstProductCategory("스포츠/레저"),
                FirstProductCategory.createFirstProductCategory("차량/오토바이"),
                FirstProductCategory.createFirstProductCategory("스타굿즈"),
                FirstProductCategory.createFirstProductCategory("키덜트"),
                FirstProductCategory.createFirstProductCategory("예술/희귀/수집품"),
                FirstProductCategory.createFirstProductCategory("음반/악기"),
                FirstProductCategory.createFirstProductCategory("도서/티켓/문구"),
                FirstProductCategory.createFirstProductCategory("뷰티/미용"),
                FirstProductCategory.createFirstProductCategory("가구/인테리어"),
                FirstProductCategory.createFirstProductCategory("생활/가공식품"),
                FirstProductCategory.createFirstProductCategory("유아동/출산"),
                FirstProductCategory.createFirstProductCategory("반려동물용품"),
                FirstProductCategory.createFirstProductCategory("기타")
        );
        List<FirstProductCategory> firstProductCategories1 = firstProductCategoryRepository.saveAll(firstProductCategories);

        Arrays.asList(
                SecondProductCategory.createSecondCategory("패딩/점퍼", firstProductCategories1.get(0)),
                SecondProductCategory.createSecondCategory("코트", firstProductCategories1.get(0)),
                SecondProductCategory.createSecondCategory("맨투맨", firstProductCategories1.get(0)),
                SecondProductCategory.createSecondCategory("후드티/후드집업", firstProductCategories1.get(0)),
                SecondProductCategory.createSecondCategory("티셔츠", firstProductCategories1.get(0)),
                SecondProductCategory.createSecondCategory("블라우스", firstProductCategories1.get(0)),
                SecondProductCategory.createSecondCategory("셔츠", firstProductCategories1.get(0)),
                SecondProductCategory.createSecondCategory("바지", firstProductCategories1.get(0)),
                SecondProductCategory.createSecondCategory("청바지", firstProductCategories1.get(0)),
                SecondProductCategory.createSecondCategory("반바지", firstProductCategories1.get(0)),
                SecondProductCategory.createSecondCategory("치마", firstProductCategories1.get(0)),
                SecondProductCategory.createSecondCategory("원피스", firstProductCategories1.get(0)),
                SecondProductCategory.createSecondCategory("가디건", firstProductCategories1.get(0)),
                SecondProductCategory.createSecondCategory("니트/스웨터", firstProductCategories1.get(0)),
                SecondProductCategory.createSecondCategory("자켓", firstProductCategories1.get(0)),
                SecondProductCategory.createSecondCategory("정장", firstProductCategories1.get(0)),
                SecondProductCategory.createSecondCategory("점프수트", firstProductCategories1.get(0)),
                SecondProductCategory.createSecondCategory("조끼/베스트", firstProductCategories1.get(0)),
                SecondProductCategory.createSecondCategory("트레이닝", firstProductCategories1.get(0)),
                SecondProductCategory.createSecondCategory("언더웨어/홈웨어", firstProductCategories1.get(0)),
                SecondProductCategory.createSecondCategory("테마/이벤트", firstProductCategories1.get(0)),
                SecondProductCategory.createSecondCategory("패딩/점퍼", firstProductCategories1.get(1)),
                SecondProductCategory.createSecondCategory("코트", firstProductCategories1.get(1)),
                SecondProductCategory.createSecondCategory("맨투맨", firstProductCategories1.get(1)),
                SecondProductCategory.createSecondCategory("후드티/후드집업", firstProductCategories1.get(1)),
                SecondProductCategory.createSecondCategory("티셔츠", firstProductCategories1.get(1)),
                SecondProductCategory.createSecondCategory("셔츠", firstProductCategories1.get(1)),
                SecondProductCategory.createSecondCategory("가디건", firstProductCategories1.get(1)),
                SecondProductCategory.createSecondCategory("니트/스웨터", firstProductCategories1.get(1)),
                SecondProductCategory.createSecondCategory("바지", firstProductCategories1.get(1)),
                SecondProductCategory.createSecondCategory("청바지", firstProductCategories1.get(1)),
                SecondProductCategory.createSecondCategory("반바지", firstProductCategories1.get(1)),
                SecondProductCategory.createSecondCategory("자켓", firstProductCategories1.get(1)),
                SecondProductCategory.createSecondCategory("정장", firstProductCategories1.get(1)),
                SecondProductCategory.createSecondCategory("조끼/베스트", firstProductCategories1.get(1)),
                SecondProductCategory.createSecondCategory("트레이닝", firstProductCategories1.get(1)),
                SecondProductCategory.createSecondCategory("언더웨어/홈웨어", firstProductCategories1.get(1)),
                SecondProductCategory.createSecondCategory("테마/이벤트", firstProductCategories1.get(1)),
                SecondProductCategory.createSecondCategory("스니커즈", firstProductCategories1.get(2)),
                SecondProductCategory.createSecondCategory("남성화", firstProductCategories1.get(2)),
                SecondProductCategory.createSecondCategory("여성화", firstProductCategories1.get(2)),
                SecondProductCategory.createSecondCategory("여성가방", firstProductCategories1.get(3)),
                SecondProductCategory.createSecondCategory("남성가방", firstProductCategories1.get(3)),
                SecondProductCategory.createSecondCategory("여행용", firstProductCategories1.get(3)),
                SecondProductCategory.createSecondCategory("시계", firstProductCategories1.get(4)),
                SecondProductCategory.createSecondCategory("주얼리", firstProductCategories1.get(4)),
                SecondProductCategory.createSecondCategory("지갑", firstProductCategories1.get(5)),
                SecondProductCategory.createSecondCategory("벨트", firstProductCategories1.get(5)),
                SecondProductCategory.createSecondCategory("모자", firstProductCategories1.get(5)),
                SecondProductCategory.createSecondCategory("목도리/장갑", firstProductCategories1.get(5)),
                SecondProductCategory.createSecondCategory("스카프/넥타이", firstProductCategories1.get(5)),
                SecondProductCategory.createSecondCategory("안경/선글라스", firstProductCategories1.get(5)),
                SecondProductCategory.createSecondCategory("양말/스타킹", firstProductCategories1.get(5)),
                SecondProductCategory.createSecondCategory("우산/양산", firstProductCategories1.get(5)),
                SecondProductCategory.createSecondCategory("기타 엑세서리", firstProductCategories1.get(5)),
                SecondProductCategory.createSecondCategory("모바일", firstProductCategories1.get(6)),
                SecondProductCategory.createSecondCategory("가전제품", firstProductCategories1.get(6)),
                SecondProductCategory.createSecondCategory("오디오/영상/관련기기", firstProductCategories1.get(6)),
                SecondProductCategory.createSecondCategory("PC/노트북", firstProductCategories1.get(6)),
                SecondProductCategory.createSecondCategory("게임/타이틀", firstProductCategories1.get(6)),
                SecondProductCategory.createSecondCategory("카메라/DSLR", firstProductCategories1.get(6)),
                SecondProductCategory.createSecondCategory("PC부품/저장장치", firstProductCategories1.get(6)),
                SecondProductCategory.createSecondCategory("골프", firstProductCategories1.get(7)),
                SecondProductCategory.createSecondCategory("캠핑", firstProductCategories1.get(7)),
                SecondProductCategory.createSecondCategory("낚시", firstProductCategories1.get(7)),
                SecondProductCategory.createSecondCategory("축구", firstProductCategories1.get(7)),
                SecondProductCategory.createSecondCategory("자전거", firstProductCategories1.get(7)),
                SecondProductCategory.createSecondCategory("인라인/스케이트보드", firstProductCategories1.get(7)),
                SecondProductCategory.createSecondCategory("전동킥보드/전동힐", firstProductCategories1.get(7)),
                SecondProductCategory.createSecondCategory("테니스", firstProductCategories1.get(7)),
                SecondProductCategory.createSecondCategory("등산/클라이밍", firstProductCategories1.get(7)),
                SecondProductCategory.createSecondCategory("헬스/요가/필라테스", firstProductCategories1.get(7)),
                SecondProductCategory.createSecondCategory("야구", firstProductCategories1.get(7)),
                SecondProductCategory.createSecondCategory("볼링", firstProductCategories1.get(7)),
                SecondProductCategory.createSecondCategory("배드민턴", firstProductCategories1.get(7)),
                SecondProductCategory.createSecondCategory("탁구", firstProductCategories1.get(7)),
                SecondProductCategory.createSecondCategory("농구", firstProductCategories1.get(7)),
                SecondProductCategory.createSecondCategory("당구", firstProductCategories1.get(7)),
                SecondProductCategory.createSecondCategory("겨울 스포츠", firstProductCategories1.get(7)),
                SecondProductCategory.createSecondCategory("수상 스포츠", firstProductCategories1.get(7)),
                SecondProductCategory.createSecondCategory("기타 구기 스포츠", firstProductCategories1.get(7)),
                SecondProductCategory.createSecondCategory("국산차", firstProductCategories1.get(8)),
                SecondProductCategory.createSecondCategory("수입차", firstProductCategories1.get(8)),
                SecondProductCategory.createSecondCategory("차량 용품/부품", firstProductCategories1.get(8)),
                SecondProductCategory.createSecondCategory("오토바이/스쿠터", firstProductCategories1.get(8)),
                SecondProductCategory.createSecondCategory("오토바이 용품/부품", firstProductCategories1.get(8)),
                SecondProductCategory.createSecondCategory("산업용 차량/장비", firstProductCategories1.get(8)),
                SecondProductCategory.createSecondCategory("보이그룹", firstProductCategories1.get(9)),
                SecondProductCategory.createSecondCategory("걸그룹", firstProductCategories1.get(9)),
                SecondProductCategory.createSecondCategory("솔로(남)", firstProductCategories1.get(9)),
                SecondProductCategory.createSecondCategory("솔로(여)", firstProductCategories1.get(9)),
                SecondProductCategory.createSecondCategory("배우(남)", firstProductCategories1.get(9)),
                SecondProductCategory.createSecondCategory("배우(여)", firstProductCategories1.get(9)),
                SecondProductCategory.createSecondCategory("방송/예능/캐릭터", firstProductCategories1.get(9)),
                SecondProductCategory.createSecondCategory("피규어/인형", firstProductCategories1.get(10)),
                SecondProductCategory.createSecondCategory("레고/블럭", firstProductCategories1.get(10)),
                SecondProductCategory.createSecondCategory("프라모델", firstProductCategories1.get(10)),
                SecondProductCategory.createSecondCategory("RC/드론", firstProductCategories1.get(10)),
                SecondProductCategory.createSecondCategory("보드게임", firstProductCategories1.get(10)),
                SecondProductCategory.createSecondCategory("서바이벌건", firstProductCategories1.get(10)),
                SecondProductCategory.createSecondCategory("기타(키덜트)", firstProductCategories1.get(10)),
                SecondProductCategory.createSecondCategory("희귀/수집품", firstProductCategories1.get(11)),
                SecondProductCategory.createSecondCategory("골동품", firstProductCategories1.get(11)),
                SecondProductCategory.createSecondCategory("예술작품", firstProductCategories1.get(11)),
                SecondProductCategory.createSecondCategory("CD/DVD/LP", firstProductCategories1.get(12)),
                SecondProductCategory.createSecondCategory("악기", firstProductCategories1.get(12)),
                SecondProductCategory.createSecondCategory("도서", firstProductCategories1.get(13)),
                SecondProductCategory.createSecondCategory("문구", firstProductCategories1.get(13)),
                SecondProductCategory.createSecondCategory("기프티콘/쿠폰", firstProductCategories1.get(13)),
                SecondProductCategory.createSecondCategory("상품권", firstProductCategories1.get(13)),
                SecondProductCategory.createSecondCategory("티켓", firstProductCategories1.get(13)),
                SecondProductCategory.createSecondCategory("스킨케어", firstProductCategories1.get(14)),
                SecondProductCategory.createSecondCategory("색조메이크업", firstProductCategories1.get(14)),
                SecondProductCategory.createSecondCategory("베이스메이크업", firstProductCategories1.get(14)),
                SecondProductCategory.createSecondCategory("바디/헤어케어", firstProductCategories1.get(14)),
                SecondProductCategory.createSecondCategory("향수/아로마", firstProductCategories1.get(14)),
                SecondProductCategory.createSecondCategory("네일아트/케어", firstProductCategories1.get(14)),
                SecondProductCategory.createSecondCategory("미용소품/기기", firstProductCategories1.get(14)),
                SecondProductCategory.createSecondCategory("다이어트/이너뷰티", firstProductCategories1.get(14)),
                SecondProductCategory.createSecondCategory("남성 화장품", firstProductCategories1.get(14)),
                SecondProductCategory.createSecondCategory("가구", firstProductCategories1.get(15)),
                SecondProductCategory.createSecondCategory("인테리어", firstProductCategories1.get(15)),
                SecondProductCategory.createSecondCategory("주방용품", firstProductCategories1.get(16)),
                SecondProductCategory.createSecondCategory("생활용품", firstProductCategories1.get(16)),
                SecondProductCategory.createSecondCategory("식품", firstProductCategories1.get(16)),
                SecondProductCategory.createSecondCategory("산업용품", firstProductCategories1.get(16)),
                SecondProductCategory.createSecondCategory("베이비의류(0~2세)", firstProductCategories1.get(17)),
                SecondProductCategory.createSecondCategory("여의류(3~6세)", firstProductCategories1.get(17)),
                SecondProductCategory.createSecondCategory("여주니어의류(7~세)", firstProductCategories1.get(17)),
                SecondProductCategory.createSecondCategory("남의류(3~6세)", firstProductCategories1.get(17)),
                SecondProductCategory.createSecondCategory("남주니어의류(7~세)", firstProductCategories1.get(17)),
                SecondProductCategory.createSecondCategory("유아용품", firstProductCategories1.get(17)),
                SecondProductCategory.createSecondCategory("출산/임부용품", firstProductCategories1.get(17)),
                SecondProductCategory.createSecondCategory("교육/완구/인형", firstProductCategories1.get(17)),
                SecondProductCategory.createSecondCategory("이유용품/유아식기", firstProductCategories1.get(17)),



        )
    }
}
