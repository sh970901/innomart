const toggle = document.getElementById('toggle');
const div1 = document.getElementById('div1');
const div2 = document.getElementById('div2');

toggle.addEventListener('click', () => {
  toggle.classList.toggle('active'); // 토글 상태 변경
  div1.classList.toggle('active'); // div1 표시/숨기기
  div2.classList.toggle('active'); // div2 표시/숨기기
});

// 주어진 데이터
const datap = {
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
      }

    ],
    "receiver": "이동일"
  }
};

// DOM 요소 가져오기
const letterContainer = document.getElementById('div1');

// 데이터 기반으로 요소 추가
datap.data.letters.forEach(letter => {
  const letterElement = document.createElement('div');
  letterElement.className = 'handwritten';
  letterElement.setAttribute('data-extra', 'from-travis');



  // sender (em 요소)
  const senderElement = document.createElement('em');
  senderElement.textContent = `From ${letter.sender}`;
  letterElement.appendChild(senderElement);

  const brElement = document.createElement('br');
  letterElement.appendChild(brElement)

  // message 내용
  const messageText = document.createTextNode(letter.message);
  letterElement.appendChild(messageText);

  // 요소 추가
  letterContainer.appendChild(letterElement);
});