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


const defaultMessage = {
    "sender": "이랜드이노플",
    "senderDepart": "😊",
    "message": "안녕하세요. 24년도 이랜드 이노플과 함께 해주셔서 감사합니다. 25년도 잘 부탁 드립니다. 연말 마무리 잘 하시고 댁내 두루 평안이 가득하시길 기원합니다."
}


document.addEventListener('DOMContentLoaded', function() {

  const letterContainer = document.getElementById('div1');

  const code = document.querySelector('#employeeCode').getAttribute('data-code');


  fetch(`/letter/employee/${code}`)
  .then(response => response.json())
  .then(data => {
    data.data.letters.unshift(defaultMessage);

    /*받는사람*/
    $('#receiver').text(data.data.receiver);

    data.data.letters.forEach(letter => {
      const letterElement = document.createElement('div');
      letterElement.className = 'handwritten';
      letterElement.setAttribute('data-extra', 'from-travis');

      /*보낸 사람*/
      const senderElement = document.createElement('em');
      senderElement.textContent = `From  ${letter.sender}  (${letter.senderDepart})`;
      letterElement.appendChild(senderElement);

      const brElement = document.createElement('br');
      letterElement.appendChild(brElement)

      /*편지 내용*/
      const messageText = document.createTextNode(letter.message);
      letterElement.appendChild(messageText);

      letterContainer.appendChild(letterElement);


    });


    /*맨 아래 편지 여백 추가*/
    let newDiv = document.createElement("div");
    newDiv.style.height = "200px";
    newDiv.style.backgroundColor = "white";
    newDiv.style.marginBottom = "20px";

    letterContainer.appendChild(newDiv);


  })
  .catch(error => console.error('Error:', error));
});

