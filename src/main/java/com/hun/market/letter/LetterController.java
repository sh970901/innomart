package com.hun.market.letter;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/letter")
@Slf4j
@RequiredArgsConstructor
public class LetterController {
    private final GoogleService googleService;
    private final RedisTemplate redisCacheTemplate;

    private static final Map<String, Employee> employees = new HashMap<>();


    @PostMapping("/write/cache")
    @Scheduled(fixedRate = 3600000)
//    @Scheduled(fixedRate = 5000)
    public void writeToCache() {
        long startTime = System.nanoTime(); // 시작 시간 기록
        log.info("Redis Update 실행");

        googleService.writeToSh22tCache();

        long endTime = System.nanoTime(); // 종료 시간 기록
        double durationInSeconds = (endTime - startTime) / 1_000_000_000.0; // 초 단위로 변환
        log.info("writeToCache 실행 시간: {} 초", durationInSeconds);
    }

    @GetMapping("/employee/{id}")
    public LettersForm getLetters(@PathVariable("id") String userId){
        if (employees.get(userId) == null){
            LettersForm.from().letters(null).receiver(null).build();
        }

        SetOperations setOperations = redisCacheTemplate.opsForSet();
        Set<Letter> letters = setOperations.members(userId);

        for (Letter letter : letters){
            String message = letter.getMessage();
            message = message.replace("\n", " ");
            letter.setMessage(message);
        }

        String receiver = employees.get(userId).getName();

        return LettersForm.from().letters(letters).receiver(receiver).build();
    }

    @PostConstruct
    public void setEmployees(){
//        List<Employee> employees = new ArrayList<>();

        addString("10234954", "이랜드이노플", "김지원");
        addString("10167750", "이노플 ITO(Works Platform)", "정락훈");
        addString("10435314", "이노플 ITO(Works Platform)", "김연정");
        addString("10169880", "이노플 ITO(HR)", "김연정");
        addString("10397821", "이노플 ITO(HR)", "김대근");
        addString("10418906", "이노플 ITO(HR)", "박유진");
        addString("10439166", "이노플 ITO(HR)", "이우영");
        addString("10009906", "이노플 ITO(DS)", "이원기");
        addString("10010497", "이노플 ITO(DS)", "최택진");
        addString("10017494", "이노플 ITO(DS)", "정형영");
        addString("10030556", "이노플 ITO(DS)", "박명진");
        addString("10152735",	"이노플 ITO(DS)", "정광영");
        addString("10167747",	"이노플 ITO(DS)",	"안혜미");
        addString("10179571",	"이노플 ITO(DS)",	"김요한");
        addString("10385430",	"이노플 ITO(DS)", "한용진");
        addString("10385901",	"이노플 ITO(DS)",	"김남훈");
        addString("10410819",	"이노플 ITO(DS)",	"이재명");
        addString("10151715",	"이노플 ITO(CRM)",	"정슬비");
        addString("10151783",	"이노플 ITO(CRM)",	"윤창우");
        addString("10408490",	"이노플 ITO(CRM)",	"김대한");
        addString("10408814",	"이노플 ITO(CRM)",	"지민희");
        addString("10421334",	"이노플 ITO(CRM)",	"박인우");
        addString("10421335",	"이노플 ITO(CRM)",	"김주은");
        addString("10453282",	"이노플 ITO(BnF)",	"김상현");
        addString("10008923",	"이노플 ITO(BnF)",	"박경남");
        addString("10352365",	"이노플 ITO(BnF)",	"유정은");
        addString("10408816",	"이노플 ITO(BnF)",	"이선형");
        addString("10410821",	"이노플 ITO(BnF)",	"김민주");
        addString("10410979",	"이노플 ITO(BnF)",	"하현우");
        addString("10453292",	"이노플 ITO(Biz Platform)",	"조원주");
        addString("10151795",	"이노플 ITO(Biz Platform)",	"주민호");
        addString("10421343",	"이노플 ITO(Biz Platform)",	"한예은");
        addString("10063954",	"이노플 ITO(BI)",	"박희숙");
        addString("10119691",	"이노플 ITO(BI)",	"윤길열");
        addString("10427536",	"이노플 ITO(BI)",	"임성빈");
        addString("10410823",	"이노플 ITO(BI)",	"김다운");
        addString("10410824",	"이노플 ITO(BI)",	"윤영주");
        addString("10432244",	"이노플 ITO(BI)",	"곽슬기");
        addString("10376986",	"이노플 ITO(BC)",	"나윤서");
        addString("10438861",	"이노플 ITO(BC)",	"이미연");
        addString("10442011",	"이노플 ITO(BC)",	"김민영");
        addString("10421114",	"이노플 ITO(AA)",	"서영우");
        addString("10421333",	"이노플 ITO(AA)",	"이경무");
        addString("10421337",	"이노플 ITO(AA)",	"정유진");
        addString("10179574",	"이노플 ITO(패션SCM)",	"이나혜");
        addString("10399529",	"이노플 ITO(패션SCM)",	"박유나");
        addString("10051549",	"이노플 ITO(패션SCM)",	"이지연");
        addString("10151703",	"이노플 ITO(패션SCM)",	"강윤하");
        addString("10152739",	"이노플 ITO(패션SCM)",	"구경환");
        addString("10394493",	"이노플 ITO(패션SCM)",	"이어진");
        addString("10410805",	"이노플 ITO(패션SCM)",	"김태건");
        addString("10423581",	"이노플 ITO(패션SCM)",	"박휘진");
        addString("10436888",	"이노플 ITO(패션SCM)",	"이동훈");
        addString("10408955",	"이노플 ITO(패션FCM)",	"문예진");
        addString("10151729",	"이노플 ITO(패션FCM)",	"김민영");
        addString("10408519",	"이노플 ITO(패션FCM)",	"김경서");
        addString("10410807",	"이노플 ITO(패션FCM)",	"김창곤");
        addString("10233788",	"이노플 ITO(정보보안)",	"유태곤");
        addString("10312549",	"이노플 ITO(정보보안)",	"전승표");
        addString("10397822",	"이노플 ITO(정보보안)",	"오지희");
        addString("10439163",	"이노플 ITO(정보보안)",	"김진성");
        addString("10011468",	"이노플 ITO(인프라)",	"조중훈");
        addString("10030627",	"이노플 ITO(인프라)",	"나기홍");
        addString("10032473",	"이노플 ITO(인프라)",	"박명수");
        addString("10088241",	"이노플 ITO(인프라)",	"황준민");
        addString("10189296",	"이노플 ITO(인프라)",	"이병찬");
        addString("10421360",	"이노플 ITO(인프라)",	"구정근");
        addString("10431320",	"이노플 ITO(인프라)",	"김한울");
        addString("10448395",	"이노플 ITO(유통SCM)",	"정상열");
        addString("10014877",	"이노플 ITO(유통SCM)",	"배성경");
        addString("10193733",	"이노플 ITO(유통SCM)",	"김동익");
        addString("10348747",	"이노플 ITO(유통SCM)",	"김일중");
        addString("10404550",	"이노플 ITO(유통SCM)",	"유희곤");
        addString("10410815",	"이노플 ITO(유통SCM)",	"이정은");
        addString("10421339",	"이노플 ITO(유통SCM)",	"김휘수");
        addString("10421340",	"이노플 ITO(유통SCM)",	"양준영");
        addString("10394827",	"이노플 ITO(유통FCM)",	"이세화");
        addString("10405327",	"이노플 ITO(유통FCM)",	"정준영");
        addString("10409568",	"이노플 ITO(유통FCM)",	"조영훈");
        addString("10421338",	"이노플 ITO(유통FCM)",	"이예진");
        addString("10042651",	"이노플 ITO(영업기획)",	"김미리");
        addString("10431858",	"이노플 ITO(건설)",	"최규원");
        addString("10439167",	"이노플 ITO(건설)",	"이가은");
        addString("10039027",	"이노플 InfraTech",	"유규성");
        addString("10133549",	"이노플 ERP사업부",	"방현종");
        addString("10063889",	"이노플 DX사업부",	"김기덕");
        addString("10391433",	"이노플 DT사업부(UI/UX디자인서비스)",	"홍은지");
        addString("10402875",	"이노플 DT사업부(UI/UX디자인서비스)",	"윤민혜");
        addString("10405326",	"이노플 DT사업부(UI/UX디자인서비스)",	"허아름");
        addString("10407630",	"이노플 DT사업부(UI/UX디자인서비스)",	"채세윤");
        addString("10080860",	"이노플 DT사업부(RPA PJ)",	"박고은");
        addString("10410813",	"이노플 DT사업부(RPA PJ)",	"유하연");
        addString("10352348",	"이노플 DT사업부(LMS PJ)",	"김정환");
        addString("10393885",	"이노플 DT사업부(LMS PJ)",	"이혜림");
        addString("10453288",	"이노플 DT사업부(INNOERP PJ)",	"이건회");
        addString("10453290",	"이노플 DT사업부(INNOERP PJ)",	"김가경");
        addString("10179579",	"이노플 DT사업부(INNOERP PJ)",	"최은영");
        addString("10355431",	"이노플 DT사업부(INNOERP PJ)",	"양선하");
        addString("10397870",	"이노플 DT사업부(INNOERP PJ)",	"이태훈");
        addString("10404816",	"이노플 DT사업부(INNOERP PJ)",	"이재혁");
        addString("10410978",	"이노플 DT사업부(INNOERP PJ)", "조한빈");
        addString("10001971",	"이노플 DT사업부(품질서비스)",	"윤여옥");
        addString("10337219",	"이노플 DT사업부(품질서비스)",	"김지영");
        addString("10011229",	"이노플 DT사업부(품질서비스)",	"정락은");
        addString("10397068",	"이노플 DT사업부(품질서비스)",	"이동훈");
        addString("10115339",	"이노플 DT사업부(클라우드 PJ)",	"김희석");
        addString("10453757",	"이노플 DT사업부(클라우드 PJ)",	"은희창");
        addString("10453293",	"이노플 DT사업부(클라우드 PJ)",	"오영주");
        addString("10453296",	"이노플 DT사업부(클라우드 PJ)",	"나예원");
        addString("10151771",	"이노플 DT사업부(클라우드 PJ)",	"박경태");
        addString("10342052",	"이노플 DT사업부(클라우드 PJ)",	"이상영");
        addString("10397844",	"이노플 DT사업부(클라우드 PJ)",	"구자성");
        addString("10421347",	"이노플 DT사업부(클라우드 PJ)",	"이호성");
        addString("10422235",	"이노플 DT사업부(클라우드 PJ)",	"공수재");
        addString("10424887",	"이노플 DT사업부(클라우드 PJ)",	"김혜원");
        addString("10397059",	"이노플 DT사업부(온라인마케팅 PJ)",	"이동일");
        addString("10432245",	"이노플 DT사업부(온라인마케팅 PJ)",	"이애린");
        addString("10445263",	"이노플 DT사업부(온라인마케팅 PJ)",	"문태영");
        addString("10453755",	"이노플 DT사업부(애슐리커머스 PJ)",	"최준아");
        addString("10052521",	"이노플 DT사업부(애슐리커머스 PJ)",	"김선익");
        addString("10395605",	"이노플 DT사업부(애슐리커머스 PJ)",	"김효미");
        addString("10397056",	"이노플 DT사업부(애슐리커머스 PJ)",	"조성수");
        addString("10397066",	"이노플 DT사업부(애슐리커머스 PJ)",	"신장수");
        addString("10413101",	"이노플 DT사업부(애슐리커머스 PJ)",	"김혜림");
        addString("10421345",	"이노플 DT사업부(애슐리커머스 PJ)",	"안유진");
        addString("10421346",	"이노플 DT사업부(애슐리커머스 PJ)","박지호");
        addString("10426131",	"이노플 DT사업부(애슐리커머스 PJ)",	"이준영");
        addString("10397820",	"이노플 DT사업부(모바일 PJ)",	"정재호");
        addString("10439165",	"이노플 DT사업부(모바일 PJ)",	"박민서");
        addString("10352369",	"이노플 DT사업부(매장솔루션 PJ)",	"황인수");
        addString("10410817",	"이노플 DT사업부(매장솔루션 PJ)",	"탁동완");
        addString("10432828",	"이노플 DT사업부(매장솔루션 PJ)",	"계경민");
        addString("10049480",	"이노플 DT사업부(기술서비스)",	"황용호");
        addString("10152730",	"이노플 DT사업부(기술서비스)",	"조희수");
        addString("10454565",	"이노플 DT사업부",	"최원창");
        addString("10447617",	"이노플 AI빅데이터사업부(Data Science)",	"문지현");
        addString("10340776",	"이노플 AI빅데이터사업부(Data Science)",	"임환승");
        addString("10348209",	"이노플 AI빅데이터사업부(Data Science)",	"김현재");
        addString("10355458",	"이노플 AI빅데이터사업부(Data Science)",	"김한나");
        addString("10404815",	"이노플 AI빅데이터사업부(Data Science)",	"김시현");
        addString("10421336",	"이노플 AI빅데이터사업부(Data Science)",	"이서정");
        addString("10421342",	"이노플 AI빅데이터사업부(Data Science)",	"김주희");
        addString("10440006",	"이노플 AI빅데이터사업부(Data Science)",	"김민서");
        addString("10400953",	"이노플 AI빅데이터사업부(Data Platform)",	"이수민");
        addString("10152851",	"이노플 AI빅데이터사업부(Data Platform)",	"홍슬기");
        addString("10410822",	"이노플 AI빅데이터사업부(Data Platform)",	"김수민");
        addString("10399522",	"이노플 커머스ITO(UI/FE개발)",	"이환");
        addString("10402233",	"이노플 커머스ITO(UI/FE개발)",	"권지희");
        addString("10404827",	"이노플 커머스ITO(UI/FE개발)",	"김단희");
        addString("10449181",	"이노플 커머스ITO(주문개발)",	"박형준");
        addString("10406999",	"이노플 커머스ITO(주문개발)",	"서지원");
        addString("10407629",	"이노플 커머스ITO(주문개발)",	"정한길");
        addString("10422894",	"이노플 커머스ITO(주문개발)",	"김수진");
        addString("10430085",	"이노플 커머스ITO(주문개발)",	"유민나");
        addString("10430456",	"이노플 커머스ITO(주문개발)",	"박민우");
        addString("10432242",	"이노플 커머스ITO(주문개발)",	"민경대");
        addString("10397062",	"이노플 커머스ITO(전시개발)",	"허은아");
        addString("10399526",	"이노플 커머스ITO(전시개발)",	"김태형");
        addString("10403150",	"이노플 커머스ITO(전시개발)",	"제어진");
        addString("10406452",	"이노플 커머스ITO(전시개발)",	"김동우");
        addString("10409232",	"이노플 커머스ITO(전시개발)",	"오홍근");
        addString("10425853",	"이노플 커머스ITO(전시개발)",	"홍은애");
        addString("10435312",	"이노플 커머스ITO(전시개발)",	"임광민");
        addString("10397580",	"이노플 커머스ITO(아키텍처)",	"김세민");
        addString("10421344",	"이노플 커머스ITO(아키텍처)",	"킹");
        addString("10424889",	"이노플 커머스ITO(아키텍처)",	"이은지");
        addString("10433285",	"이노플 커머스ITO(아키텍처)",	"구은현");
        addString("10436197",	"이노플 커머스ITO(아키텍처)",	"장재휴");
        addString("10409603",	"이노플 커머스ITO(상품/업체개발)",	"유영기");
        addString("10422889",	"이노플 커머스ITO(상품/업체개발)",	"김진수");
        addString("10423584",	"이노플 커머스ITO(상품/업체개발)",	"연규환");
        addString("10432240",	"이노플 커머스ITO(상품/업체개발)",	"김성수");
        addString("10408086",	"이노플 커머스ITO(데이터)",	"양효빈");
        addString("10410808",	"이노플 커머스ITO(데이터)",	"정지은");
        addString("10411955",	"이노플 커머스ITO(데이터)",	"한유지");
        addString("10399322",	"이노플 커머스사업부",	"정지원");
        addString("10454581",	"이노플 올리브스튜디오(콘텐츠)",	"김산성");
        addString("10432028",	"이노플 올리브스튜디오(콘텐츠)",	"안서희");
        addString("10443549",	"이노플 올리브스튜디오(콘텐츠)",	"권지혜");
        addString("10443546",	"이노플 올리브스튜디오(라이선스 사업&마케팅)",	"장시연");
        addString("10445954",	"이노플 올리브스튜디오(라이선스 사업&마케팅)",	"장현희");
        addString("10442017",	"이노플 오픈이노베이션랩(신사업기획)",	"서원");
        addString("10103950",	"이노플 오픈이노베이션랩",	"민혁");
        addString("10133545",	"이노플 싸인투게더",	"김재원");
        addString("10391384",	"이노플 싸인투게더",	"김지회");
        addString("10401392",	"이노플 싸인투게더",	"김종훈");
        addString("10410811",	"이노플 싸인투게더",	"정동호");
        addString("10420425",	"이노플 본부(채용교육)",	"김주혜");
        addString("10397085",	"이노플 본부(재무)",	"김정철");
        addString("10420494",	"이노플 본부(인사)", "조민주");
        addString("10347092",	"이노플 본부(법무)",	"김보민");
        addString("10414409",	"이노플 본부(기획)",	"김선영");
        addString("111111111",	"이노플 총무", "이용채");
        addString("10426472", "이노플", "홍은수");
        addString("12341234", "이노플", "테스트사번");


    }

    private void addString(String idenNum, String depart, String name){
        Employee employee = Employee.from().idenNum(idenNum).name(name).build();
        employees.put(idenNum, employee);
    }
}
