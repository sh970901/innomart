//Magic goes here...

var cat = document.getElementById("cat"),
    shadowBack = document.getElementsByClassName("shadow-2"),
    shadowMed = document.getElementsByClassName("shadow-1"),
    tlc = new TimelineLite();
tl = new TimelineLite();

tlc.fromTo(
    cat,
    1,
    { opacity: 1, y: 60 },
    { opacity: 1, y: 0, delay: 2, ease: Power4.easeOut }
);

tl
.fromTo(
    shadowBack,
    2,
    { opacity: 0 },
    { opacity: 0.5, repeat: -1, yoyo: true, delay: 0.5 }
)
.fromTo(
    shadowMed,
    2,
    { opacity: 0.25 },
    { opacity: 0.5, repeat: -1, yoyo: true }
);

cat.onmouseover = function (e) {
  tlc.reverse();
};
cat.onmouseout = function (e) {
  tlc.play();
};


const data = {
  "resultCode": "200",
  "resultMessage": "success",
  "data": {
    "letters": [
      {
        "sender": "이환",
        "senderDepart": "커머스ITO(UI/FE개발)",
        "message": "모든 일에 최선을 다하고 기분 좋은 에너지를 주셔서 좋았습니다. 새해에 좋은 일이 가득하길 기원합니다"
      },
      {
        "sender": "공수재",
        "senderDepart": "클라우드",
        "message": "동일아. 항상 열심히 하는 모습이 참 보기 좋다. 반드시 보답될 거다. 화이팅!"
      },
      {
        "sender": "윤창우",
        "senderDepart": "ITO CRM팀",
        "message": "갑작스럽게 팀장 되면서 같이 일하던 동료에서 팀장으로 대하기 어려운 부분도 있었겠지만, 주도적으로 해주셔서 많은 수고를 덜게 된 것 같습니다. 항상 분위기 잘 이끌어주시고 도전적인 과업을 맡고 계시지만 내년에 성과 많이 날거라고 믿어 의심치 않습니다.올해도 수고 많으셨고 내년엔 더 성과 많도록 같이 잘해 봐요. 올해도 내년도 감사합니다."
      },
      {
        "sender": "윤창우",
        "senderDepart": "ITO CRM팀",
        "message": "갑작스럽게 팀장 되면서 같이 일하던 동료에서 팀장으로 대하기 어려운 부분도 있었겠지만, 주도적으로 해주셔서 많은 수고를 덜게 된 것 같습니다. 항상 분위기 잘 이끌어주시고 도전적인 과업을 맡고 계시지만 내년에 성과 많이 날거라고 믿어 의심치 않습니다.올해도 수고 많으셨고 내년엔 더 성과 많도록 같이 잘해 봐요. 올해도 내년도 감사합니다."
      },
      {
        "sender": "윤창우",
        "senderDepart": "ITO CRM팀",
        "message": "갑작스럽게 팀장 되면서 같이 일하던 동료에서 팀장으로 대하기 어려운 부분도 있었겠지만, 주도적으로 해주셔서 많은 수고를 덜게 된 것 같습니다. 항상 분위기 잘 이끌어주시고 도전적인 과업을 맡고 계시지만 내년에 성과 많이 날거라고 믿어 의심치 않습니다.올해도 수고 많으셨고 내년엔 더 성과 많도록 같이 잘해 봐요. 올해도 내년도 감사합니다."
      },
      {
        "sender": "윤창우",
        "senderDepart": "ITO CRM팀",
        "message": "갑작스럽게 팀장 되면서 같이 일하던 동료에서 팀장으로 대하기 어려운 부분도 있었겠지만, 주도적으로 해주셔서 많은 수고를 덜게 된 것 같습니다. 항상 분위기 잘 이끌어주시고 도전적인 과업을 맡고 계시지만 내년에 성과 많이 날거라고 믿어 의심치 않습니다.올해도 수고 많으셨고 내년엔 더 성과 많도록 같이 잘해 봐요. 올해도 내년도 감사합니다."
      },
      {
        "sender": "윤창우",
        "senderDepart": "ITO CRM팀",
        "message": "갑작스럽게 팀장 되면서 같이 일하던 동료에서 팀장으로 대하기 어려운 부분도 있었겠지만, 주도적으로 해주셔서 많은 수고를 덜게 된 것 같습니다. 항상 분위기 잘 이끌어주시고 도전적인 과업을 맡고 계시지만 내년에 성과 많이 날거라고 믿어 의심치 않습니다.올해도 수고 많으셨고 내년엔 더 성과 많도록 같이 잘해 봐요. 올해도 내년도 감사합니다."
      },
      {
        "sender": "윤창우",
        "senderDepart": "ITO CRM팀",
        "message": "갑작스럽게 팀장 되면서 같이 일하던 동료에서 팀장으로 대하기 어려운 부분도 있었겠지만, 주도적으로 해주셔서 많은 수고를 덜게 된 것 같습니다. 항상 분위기 잘 이끌어주시고 도전적인 과업을 맡고 계시지만 내년에 성과 많이 날거라고 믿어 의심치 않습니다.올해도 수고 많으셨고 내년엔 더 성과 많도록 같이 잘해 봐요. 올해도 내년도 감사합니다."
      },
      {
        "sender": "윤창우",
        "senderDepart": "ITO CRM팀",
        "message": "갑작스럽게 팀장 되면서 같이 일하던 동료에서 팀장으로 대하기 어려운 부분도 있었겠지만, 주도적으로 해주셔서 많은 수고를 덜게 된 것 같습니다. 항상 분위기 잘 이끌어주시고 도전적인 과업을 맡고 계시지만 내년에 성과 많이 날거라고 믿어 의심치 않습니다.올해도 수고 많으셨고 내년엔 더 성과 많도록 같이 잘해 봐요. 올해도 내년도 감사합니다."
      },
      {
        "sender": "윤창우",
        "senderDepart": "ITO CRM팀",
        "message": "갑작스럽게 팀장 되면서 같이 일하던 동료에서 팀장으로 대하기 어려운 부분도 있었겠지만, 주도적으로 해주셔서 많은 수고를 덜게 된 것 같습니다. 항상 분위기 잘 이끌어주시고 도전적인 과업을 맡고 계시지만 내년에 성과 많이 날거라고 믿어 의심치 않습니다.올해도 수고 많으셨고 내년엔 더 성과 많도록 같이 잘해 봐요. 올해도 내년도 감사합니다."
      }
    ],
    "receiver": "이동일"
  }
};

const letters = data.data.letters;

const positions = [
/*  { cx: 146, cy: 256 },
  { cx: 196, cy: 286 },
  { cx: 370, cy: 256 },
  { cx: 320, cy: 286 },
  { cx: 230, cy: 296 },
  { cx: 196, cy: 216 },
  { cx: 320, cy: 216 },
  { cx: 230, cy: 226 },
  { cx: 196, cy: 156 },
  { cx: 320, cy: 156 },
  { cx: 230, cy: 166 },*/
   /*신규위치*/
/*  { cx: 230, cy: 116 },
  { cx: 295, cy: 116 }
  { cx: 230, cy: 145 },*/
  { cx: 290, cy: 145 },
  { cx: 225, cy: 156 },
  { cx: 290, cy: 200 },
  { cx: 345, cy: 230 },
  { cx: 290, cy: 260 },
  { cx: 170, cy: 230 },
  { cx: 165, cy: 295 },
  { cx: 230, cy: 275 },
  { cx: 285, cy: 315 },
  { cx: 355, cy: 295 }
];

const images = [
  "https://innomarket.s3.ap-northeast-2.amazonaws.com/cake.png",
  "https://innomarket.s3.ap-northeast-2.amazonaws.com/giftbox.png",
  "https://innomarket.s3.ap-northeast-2.amazonaws.com/snowman.png",
  "https://innomarket.s3.ap-northeast-2.amazonaws.com/candy.png",
  "https://innomarket.s3.ap-northeast-2.amazonaws.com/reindeer.png",
  "https://innomarket.s3.ap-northeast-2.amazonaws.com/candle.png",
  "https://innomarket.s3.ap-northeast-2.amazonaws.com/gift.png",
  "https://innomarket.s3.ap-northeast-2.amazonaws.com/santa.png",
  "https://innomarket.s3.ap-northeast-2.amazonaws.com/globe.png",
  "https://innomarket.s3.ap-northeast-2.amazonaws.com/sock.png",
  "https://innomarket.s3.ap-northeast-2.amazonaws.com/snowsnow.png",
  "https://innomarket.s3.ap-northeast-2.amazonaws.com/cookie.png",
];
let usedPositions = [];

function placeRandomElement(letter) {
  if (usedPositions.length >= positions.length) {
    return;
  }

  // 🎯 중복되지 않는 위치 선택
  let randomPosition;
  do {
    randomPosition = positions[Math.floor(Math.random() * positions.length)];
  } while (usedPositions.some(pos => pos.cx === randomPosition.cx && pos.cy === randomPosition.cy));

  usedPositions.push(randomPosition);

  if (images.length === 0) {
    return;
  }

  // 🎯 랜덤 이미지 선택
  const randomImageIndex = Math.floor(Math.random() * images.length);
  const randomImage = images.splice(randomImageIndex, 1)[0];

  // 🎉 그룹 요소 생성 (이미지와 텍스트를 묶기 위해)
  const groupElement = document.createElementNS("http://www.w3.org/2000/svg", "g");

  // ✨ 이미지 추가
  const imageElement = document.createElementNS("http://www.w3.org/2000/svg", "image");
  imageElement.setAttribute("x", randomPosition.cx - 15); // 이미지 중심 정렬
  imageElement.setAttribute("y", randomPosition.cy - 25); // 이미지 중심 정렬
  imageElement.setAttribute("width", "30");
  imageElement.setAttribute("height", "30");
  imageElement.setAttribute("href", randomImage);
  imageElement.setAttribute("class", "random-image");
  groupElement.appendChild(imageElement);

  // ✨ 텍스트 추가
  const textElement = document.createElementNS("http://www.w3.org/2000/svg", "text");
  textElement.setAttribute("x", randomPosition.cx); // 텍스트 중심 정렬
  textElement.setAttribute("y", randomPosition.cy + 15); // 이미지 아래로 살짝 내림
  textElement.setAttribute("class", "random-text");
  textElement.setAttribute("data-content", letter.message); // 메시지를 data-content에 추가
  textElement.setAttribute("text-anchor", "middle"); // 중앙 정렬
  textElement.setAttribute("dominant-baseline", "middle"); // y축 중앙 정렬
  textElement.textContent = letter.sender;

  // 🎨 스타일 적용
  textElement.style.fontSize = "12px"; // 폰트 크기
  textElement.style.fill = "white"; // 글씨 색상 (흰색)
  textElement.style.fontFamily = "SpoqaHanSansNeo-Regular"; // 폰트 스타일
  textElement.style.fontWeight = "600"; // 글씨 굵게 (bold)
  textElement.style.filter = "url(#text-shadow)"; // 드롭 섀도우 필터 적용

  groupElement.appendChild(textElement); // 텍스트를 그룹에 추가

  // 🔥 클릭 이벤트 추가 (그룹 클릭 시 메시지 표시)
  groupElement.addEventListener('click', function() {
    alert(`보낸 사람: ${letter.sender}\n부서: ${letter.senderDepart}\n메시지: ${letter.message}`);
  });

  // 🔥 기본 동작 막기 (커서 제거)
  groupElement.addEventListener('mousedown', (event) => {
    event.preventDefault(); // 클릭 시 기본 동작 차단
  });

  // 🎉 그룹을 SVG에 추가
  document.querySelector('svg').appendChild(groupElement);
}


letters.forEach(letter => placeRandomElement(letter));