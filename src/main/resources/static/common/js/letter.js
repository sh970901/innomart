const toggle = document.getElementById('toggle');
const div1 = document.getElementById('div1');
const div2 = document.getElementById('div2');

/*토글 변경 이벤트*/
toggle.addEventListener('click', (e) => {
  e.preventDefault();
  toggle.classList.toggle('active');
  div1.classList.toggle('active');
  div2.classList.toggle('active');
});





document.addEventListener('DOMContentLoaded', function() {

  const letterContainer = document.getElementById('div1');

  const code = document.querySelector('#employeeCode').getAttribute('data-code');


  fetch(`/letter/employee/${code}`)
  .then(response => response.json())
  .then(data => {


    const defaultMessage = {
      "sender": "이랜드이노플",
      "senderDepart": "😊",
      "message" : "안녕하세요 " + data.data.receiver + "님!\n\n2024년도 이랜드 이노플과 \n 함께 해주셔서 감사합니다.\n\n"
          + "다사다난 했던 한해 " + data.data.receiver + "님과\n\ 함께여서 행복했습니다."
          + "\n\n25년도 잘 부탁 드립니다.\n\n남은 연말 마무리 잘 하시고 \n댁내 두루 평안이 가득하시길 기원합니다.\n\n 💗행복하세요💗"
    }
    data.data.letters.unshift(defaultMessage);

    /*받는사람*/
    $('#receiver').text(data.data.receiver);

    data.data.letters.forEach(letter => {
      letterContainer.appendChild(createLetterElement(letter));
    });
    createRandomElement(data.data.letters);


    /*맨 아래 편지 여백 추가*/
    let newDiv = document.createElement("div");
    newDiv.style.height = "200px";
    newDiv.style.backgroundColor = "white";
    newDiv.style.marginBottom = "20px";

    letterContainer.appendChild(newDiv);


  })
  .catch(error => console.error('Error:', error));
});


/*편지 객체 생성*/
function createLetterElement(letter) {
  const letterElement = document.createElement('div');
  letterElement.className = 'handwritten';
  letterElement.setAttribute('data-extra', 'from-travis');

  // 🎉 랜덤으로 1부터 11까지의 숫자를 생성
  const randomNum = Math.floor(Math.random() * 11) + 1; // 1 ~ 11
  const backgroundUrl = `https://innomarket.s3.ap-northeast-2.amazonaws.com/letterpaper${randomNum}.PNG`;

  // ✨ 배경 이미지 동적 추가
  letterElement.style.backgroundImage = `url('${backgroundUrl}')`;



  letterElement.style.backgroundPosition = 'center';
  letterElement.style.backgroundRepeat = 'no-repeat';

  // 보낸 사람
  const senderElement = document.createElement('em');
  senderElement.textContent = `From  ${letter.sender}  (${letter.senderDepart})`;
  letterElement.appendChild(senderElement);

  const brElement = document.createElement('br');
  letterElement.appendChild(brElement);

  // 편지 내용
  const formattedMessage = letter.message.replace(/(?:\r\n|\r|\n)/g, '<br>'); // 줄바꿈을 <br>로 변환
  letterElement.innerHTML += formattedMessage; // innerHTML로 메시지 추가

  return letterElement;
}



/*랜덤오브제생성*/
function placeRandomElement(letter) {
  if (usedPositions.length >= positions.length) {
    return;
  }

  // 🔥 1️⃣ 뷰포트 크기 동적 계산 (모바일, PC 겸용)
  const svgElement = document.querySelector('svg');
  const svgWidth = svgElement.clientWidth;
  const svgHeight = svgElement.clientHeight;

  // 📍 2️⃣ 중복 위치 방지: 랜덤 위치 계산 (뷰포트 크기 비율로)
  let randomPosition;
  do {
    randomPosition = positions[Math.floor(Math.random() * positions.length)];
  } while (usedPositions.some(pos => pos.cx === randomPosition.cx && pos.cy === randomPosition.cy));

  usedPositions.push(randomPosition);

  if (images.length === 0) {
    return;
  }

  // 🎉 3️⃣ 랜덤 오브제 선택
  const randomImageIndex = Math.floor(Math.random() * images.length);
  const randomImage = images.splice(randomImageIndex, 1)[0];

  // 🎉 4️⃣ 그룹 요소 생성 (이미지와 텍스트를 묶기 위해)
  const groupElement = document.createElementNS("http://www.w3.org/2000/svg", "g");

  // ✨ 5️⃣ 이미지 추가
  const imageSize = 30; // 이미지 크기
  const imageElement = document.createElementNS("http://www.w3.org/2000/svg", "image");
  imageElement.setAttribute("x", randomPosition.cx - imageSize / 2);
  imageElement.setAttribute("y", randomPosition.cy - imageSize / 2);
  imageElement.setAttribute("width", imageSize);
  imageElement.setAttribute("height", imageSize);
  imageElement.setAttribute("href", randomImage);
  imageElement.setAttribute("class", "random-image");
  imageElement.style.willChange = 'transform, opacity'; // 하드웨어 가속
  groupElement.appendChild(imageElement);

  // 🆕 ✨ 텍스트 박스 추가 (검정 투명 배경)
  const textWidth = 40;
  const textHeight = 20;
  const rectElement = document.createElementNS("http://www.w3.org/2000/svg", "rect");
  rectElement.setAttribute("x", randomPosition.cx - textWidth / 2);
  rectElement.setAttribute("y", randomPosition.cy + imageSize / 2 + 7);
  rectElement.setAttribute("width", textWidth);
  rectElement.setAttribute("height", textHeight);
  rectElement.setAttribute("rx", 5);
  rectElement.setAttribute("ry", 5);
  rectElement.setAttribute("fill", "rgba(0, 0, 0, 0.5)");
  groupElement.appendChild(rectElement);

  // ✨ 6️⃣ 텍스트 추가
  const textElement = document.createElementNS("http://www.w3.org/2000/svg", "text");
  textElement.setAttribute("x", randomPosition.cx);
  textElement.setAttribute("y", randomPosition.cy + imageSize / 2 + textHeight / 2 + 4.5);
  textElement.setAttribute("class", "random-text");
  textElement.setAttribute("data-content", letter.message);
  textElement.setAttribute("text-anchor", "middle");
  textElement.setAttribute("dominant-baseline", "middle");
  textElement.setAttribute("dy", "0.35em"); // iOS 호환성 보완
  textElement.textContent = letter.sender;

  // 🎨 7️⃣ 스타일 추가
  textElement.style.fontSize = "12px";
  textElement.style.fill = "white";
  textElement.style.fontWeight = "600";
  textElement.style.filter = "url(#text-shadow)";
  textElement.style.fontFamily = "Arial, sans-serif";
  textElement.style.willChange = 'transform, opacity';
  groupElement.appendChild(textElement);

  // 🔥 8️⃣ 클릭 이벤트 추가
  groupElement.addEventListener('click', function() {
    openModal(letter);
  });

  // 🔥 9️⃣ 기본 동작 방지 (PC, 모바일 모두 대응)
  groupElement.addEventListener('mousedown', (event) => event.preventDefault());
  groupElement.addEventListener('touchstart', (event) => event.preventDefault()); // 모바일 대응

  // 🎉 10️⃣ SVG에 그룹 추가
  requestAnimationFrame(() => {
    document.querySelector('svg').appendChild(groupElement);
    groupElement.style.opacity = "1"; // 강제 리렌더링
  });
}



function createRandomElement(letters) {
  /*랜덤 오브제 뿌리기 */
  letters.forEach(letter => placeRandomElement(letter));
}


const positions = [

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


// ✨ 모달 요소 참조
const modalOverlay = document.querySelector('.modal-overlay');
const modalContent = document.querySelector('.modal-content');
const closeButton = document.querySelector('.close-button');

// 🎉 모달 열기
function openModal(content) {
  // 모달 내용 초기화 (기존 내용 삭제)
  modalContent.innerHTML = ''; // 혹시 기존에 들어있는 내용이 있으면 삭제

  // 클릭한 편지 내용을 createLetterElement 함수로 가져오기
  const letterElement = createLetterElement(content);

  // 모달 내용에 letterElement 추가
  modalContent.appendChild(letterElement);

  // 모달 표시
  modalOverlay.style.display = 'flex';
}

// 🔥 모달 닫기
function closeModal() {
  modalOverlay.style.display = 'none'; // 모달 숨기기
}

// 🛠️ 모달 닫기 버튼 클릭 이벤트
closeButton.addEventListener('click', closeModal);

// 🛠️ 모달 외부 클릭 시 닫기
modalOverlay.addEventListener('click', (event) => {
  if (event.target === modalOverlay) closeModal();
});