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



/* 랜덤 오브제 생성 */
function placeRandomElement(letter) {
  if (usedPositions.length >= positions.length) {
    return;
  }

  const svgElement = document.querySelector('svg');
  const svgWidth = svgElement.clientWidth;
  const svgHeight = svgElement.clientHeight;

  let randomPosition;
  do {
    randomPosition = positions[Math.floor(Math.random() * positions.length)];
  } while (usedPositions.some(pos => pos.cx === randomPosition.cx && pos.cy === randomPosition.cy));

  usedPositions.push(randomPosition);

  if (images.length === 0) {
    return;
  }

  const randomImageIndex = Math.floor(Math.random() * images.length);
  const randomImage = images.splice(randomImageIndex, 1)[0];

  const groupElement = document.createElementNS("http://www.w3.org/2000/svg", "g");

  const imageSize = 30;
  const imageElement = document.createElementNS("http://www.w3.org/2000/svg", "image");
  imageElement.setAttribute("x", randomPosition.cx - imageSize / 2);
  imageElement.setAttribute("y", randomPosition.cy - imageSize / 2);
  imageElement.setAttribute("width", imageSize);
  imageElement.setAttribute("height", imageSize);
  imageElement.setAttribute("href", randomImage);
  imageElement.setAttribute("class", "random-image");
  imageElement.style.willChange = 'transform, opacity';
  groupElement.appendChild(imageElement);

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

  const textElement = document.createElementNS("http://www.w3.org/2000/svg", "text");
  textElement.setAttribute("x", randomPosition.cx);
  textElement.setAttribute("y", randomPosition.cy + imageSize / 2 + textHeight / 2 + 9); // y 위치 조정
  textElement.setAttribute("class", "random-text");
  textElement.setAttribute("data-content", letter.message);
  textElement.setAttribute("text-anchor", "middle");
  textElement.setAttribute("alignment-baseline", "middle"); // 사파리 호환
  textElement.setAttribute("font-family", "Arial, sans-serif"); // 명시적 폰트 설정
  textElement.setAttribute("font-size", "12px"); // 폰트 크기
  textElement.setAttribute("fill", "white"); // fill 명시적 적용
  textElement.textContent = letter.sender;

  groupElement.appendChild(textElement);
  textElement.style.fontWeight = "600";
  groupElement.style.pointerEvents = 'auto';
  groupElement.style.touchAction = 'manipulation';
  groupElement.style.userSelect = 'none';
  groupElement.style.cursor = 'pointer';

  groupElement.addEventListener('click', function() {
    openModal(letter);
  });

  groupElement.addEventListener('touchstart', (event) => {
    event.preventDefault();
    openModal(letter);
  }, { passive: true });

  groupElement.addEventListener('mousedown', (event) => event.preventDefault());
  groupElement.addEventListener('touchstart', (event) => event.preventDefault());

  requestAnimationFrame(() => {
    document.querySelector('svg').appendChild(groupElement);
    groupElement.style.opacity = "1";
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