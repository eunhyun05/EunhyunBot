package io.github.eunhyun.eunhyunbot.api.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KoreanChannelNameGenerator {

    private static final List<String> TOPICS = List.of(
            "지혜로운", "행복한", "평화로운", "활기찬", "신비로운", "우아한", "빛나는", "즐거운", "멋진", "사랑스러운",
            "환상적인", "아름다운", "감동적인", "풍요로운", "상쾌한", "자유로운", "조용한", "신나는", "창의적인", "용감한",
            "차분한", "매력적인", "활발한", "빛나는", "로맨틱한", "신속한", "단정한", "편안한", "유쾌한", "명랑한",
            "정열적인", "정교한", "사색적인", "미래지향적인", "격려하는", "성실한", "친절한", "지적인", "긍정적인", "자연스러운",
            "고요한", "흥미로운", "놀라운", "색다른", "강렬한", "부드러운", "여유로운", "따뜻한", "진지한", "소중한",
            "매우 기쁜", "새로운", "편리한", "쾌적한", "신중한", "근면한", "친밀한", "천천히 흐르는", "영감을 주는", "기대되는",
            "효율적인", "즐거운", "자상한", "평온한", "정직한", "사랑받는", "리듬감 있는", "안락한", "사랑스럽고", "행복한"
    );

    private static final List<String> DESCRIPTIONS = List.of(
            "생활", "모험", "세계", "이야기", "정원", "마을", "축제", "시간", "여행", "공간",
            "서재", "카페", "운동", "마법", "미래", "정원", "수채화", "풍경", "탐험", "축제",
            "자연", "도서관", "연회", "휴식", "사원", "별빛", "테라스", "공원", "산책로", "풍경",
            "문학", "전시회", "모임", "해변", "도시", "아침", "밤", "기억", "기쁨", "심상",
            "별자리", "축제", "계절", "이벤트", "연극", "다시보기", "미술관", "휴일", "전통", "비밀",
            "열정", "관람", "자연풍경", "별빛", "시골", "상상", "신비", "여행기", "작품", "달빛",
            "건강", "행복", "오래된", "유산", "문화", "세계여행", "소풍", "만남", "스타일", "저녁"
    );

    private static final Random RANDOM = new Random();

    public static String generateRandomName() {
        String topic = TOPICS.get(RANDOM.nextInt(TOPICS.size()));
        String description = DESCRIPTIONS.get(RANDOM.nextInt(DESCRIPTIONS.size()));
        return topic + " " + description;
    }
}