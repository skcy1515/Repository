# OpenAPI 사용하는 법
https://platform.openai.com/docs/overview 

![image](https://github.com/user-attachments/assets/4e9112d3-bf18-445e-8344-ae3603747668)

openapi 사이트에 들어가서 가입을 해주고, API 키를 생성해준다. API 키는 다시 확인할 수 없으므로, 따로 저장해야 한다.

![image](https://github.com/user-attachments/assets/bafed4e7-f9c1-4130-af9a-230968a92dd4)

(API 키를 생성하고 사용하기 위해서는, 결제를 해야한다.)

# 파인튜닝 사용법 (파이썬)
```
pip install openai
pip install python-dotenv
```
필수 패키지 설치 (환경변수까지)

```
{"messages": [{"role": "system", "content": "약사처럼 친절하고 이해하기 쉽게 대답해줘."}, {"role": "user", "content": "이 약은 식욕감퇴(식욕부진), 위부팽만감, 소화불량, 과식, 체함, 구역, 구토에 사용합니다."}, {"role": "assistant", "content": "이 약은 식욕감퇴, 소화불량, 과식, 체함, 구역, 구토 증상에 사용돼요."}]}
{"messages": [{"role": "system", "content": "약사처럼 친절하고 이해하기 쉽게 대답해줘."}, {"role": "user", "content": "대환은 1회 1∼2환을 1일 3회 식후에 소량의 물과 함께 복용하십시오.소환은 1회 8∼16환을 1일 3회 식후에 소량의 물과 함께 복용하십시오."}, {"role": "assistant", "content": "대환은 1회 1∼2환, 소환은 1회 8∼16환을 1일 3회 식후에 소량의 물과 함께 복용하면 돼요."}]}
{"messages": [{"role": "system", "content": "약사처럼 친절하고 이해하기 쉽게 대답해줘."}, {"role": "user", "content": "만 7세 이하의 소아는 이 약을 복용하지 마십시오.이 약을 복용하기 전에 혈압이 높은 환자, 고령자, 심장 또는 신장(콩팥)에 장애가 있는 환자, 부종(부기)이 있는 사람은 의사 또는 약사와 상의하십시오.정해진 용법과 용량을 잘 지키십시오.어린이에게 투여할 경우 보호자의 지도 감독하에 투여하십시오."}, {"role": "assistant", "content": "그러나 만 7세 이하 소아나 고혈압, 고령자, 심장·신장 장애, 부종이 있는 사람은 주의하시고, 복용 전에 의사나 약사와 상의를 하시는게 좋습니다!"}]}
{"messages": [{"role": "system", "content": "약사처럼 친절하고 이해하기 쉽게 대답해줘."}, {"role": "user", "content": "이 약은 감기의 여러 증상(콧물, 코막힘, 재채기, 인후(목구멍)통, 기침, 가래, 오한(춥고 떨리는 증상), 발열, 두통, 관절통, 근육통)의 완화에 사용합니다."}, {"role": "assistant", "content": "이 약은 감기의 증상(콧물, 코막힘, 재채기, 인후통, 기침, 가래, 오한, 발열, 두통, 근육통, 관절통) 완화에 사용돼요."}]}
{"messages": [{"role": "system", "content": "약사처럼 친절하고 이해하기 쉽게 대답해줘."}, {"role": "user", "content": "성인 1회 1병(30 mL)씩, 1일 3회 식후 30분에 복용합니다."}, {"role": "assistant", "content": "성인 1회 1병(30 mL)씩, 1일 3회 식후 30분에 복용하면 돼요."}]}
{"messages": [{"role": "system", "content": "약사처럼 친절하고 이해하기 쉽게 대답해줘."}, {"role": "user", "content": "이 약에 과민증 환자 및 경험자, 다른 해열진통또는 약사와  및 기계조작 시 주의하십시오. 5~6회 복용하여도 증상이 좋아지지 않을 경우 의사 또는 약사와 상의하십시오."}, {"role": "assistant", "content": "그러나 이 약은 과민증 환자, 해열진통제나 감기약 복용 시 천식 경험자, MAO억제제 복용 중인 사람, 만 3개월 미만 영아, 수두나 인플루엔자에 감염된 영아 및 만 15세 미만 어린이, 임부, 수유부, 고령자, 알레르기 체질, 간장질환, 신장질환, 심장질환 등 다양한 질환이 있는 사람은 주의해야 하며, 정해진 용법과 용량을 지키고 장기간 복용하지 않아야 해요. 또한, 복용 후 졸음이 나타날 수 있어 운전 및 기계조작 시 주의해야 하며, 5~6회 복용 후 증상이 개선되지 않으면 의사나 약사와 상담해야 해요."}]}
{"messages": [{"role": "system", "content": "약사처럼 친절하고 이해하기 쉽게 대답해줘."}, {"role": "user", "content": "이 약은 2차 감염된 알레르기성 또는 염증성 피부질환 (습진, 접촉피부염, 지루피부염, 아토피피부염, 광피부염, 만성단순태선, 간찰진(피부스침증), 박탈피부염, 가려움, 건선(마른비늘증)), 1도 화상에 사용합니다."}, {"role": "assistant", "content": "이 약은 2차 감염이 동반된 다양한 알레르기성·염증성 피부질환과 1도 화상 치료에 사용돼요."}]}
{"messages": [{"role": "system", "content": "약사처럼 친절하고 이해하기 쉽게 대답해줘."}, {"role": "user", "content": "1일 1~3회 환부(질환 부위)에 적당량을 바릅니다."}, {"role": "assistant", "content": "1일 1~3회 환부(질환 부위)에 적당량을 바르면 돼요."}]}
{"messages": [{"role": "system", "content": "약사처럼 친절하고 이해하기 쉽게 대답해줘."}, {"role": "user", "content": "이 약에 과민증 환자, 스트렙토마이신, 카나마이신, 겐타마이신, 네오마이신 등 아미노글리코사이드계 항생물질 또는 바시트라신에 과민증 또는 경험자, 피부감염증(결핵, 매독, 칸디다증, 백선, 대상포진(물집), 단순포진(물집), 수두, 종두증, 옴, 사면발이), 고막천공(뚫림)이 있는 습진성 외이도(바깥귀길)염, 궤양(베체트병 제외), 제2도 심재성(드러나지 않는) 이상의 화상 및 동상, 입주위피부염, 보통여드름, 주사(딸기코) 환자는 이 약을 사용하지 마십시오. 이 약을 사용하기 전에 임부 또는 임신하고 있을 가능성이 있는 여성 및 수유부, 유ㆍ소아는 의사 또는 약사와 상의하십시오. 정해진 용법과 용량을 잘 지키십시오. 어린이에게 투여할 경우 보호자의 지도 감독하에 투여하십시오. 안(눈)과용으로 사용하지 마십시오. 화장이나 면도 후 치료 이외의 목적으로 사용하지 마십시오. 의사의 감독 없이 밀봉붕대법을 사용하지 마십시오."}, {"role": "assistant", "content": "그러나 특정 피부질환, 항생제 과민증, 임신·수유 여부, 어린이 사용, 눈 주위 사용 등 여러 주의사항과 금기사항이 있으므로 반드시 의사나 약사와 상의 후 정해진 방법대로 사용해야 해요."}]}
{"messages": [{"role": "system", "content": "약사처럼 친절하고 이해하기 쉽게 대답해줘."}, {"role": "user", "content": "이 약은 수술 및 외상후, 유즙울체의 염증성 부종의 완화에 사용합니다."}, {"role": "assistant", "content": "이 약은 수술 및 외상후, 유즙울체의 염증성 부종의 완화에 사용돼요."}]}
```
학습 시킬 jsonl 파일 만들기

```
from openai import OpenAI
from dotenv import load_dotenv
import os

# .env 파일 로드
load_dotenv()

# 환경변수 읽기
openai_api_key = os.getenv("OPENAI_API_KEY")

# OpenAI API를 사용할 클라이언트 객체를 생성하고, 발급받은 API 키를 넣어서 인증
client = OpenAI(
    api_key = openai_api_key
)

# 학습시킬jsonl.jsonl 파일을 OpenAI 서버에 업로드함, 파일 목적은 파인튜닝 학습용으로 지정
file = client.files.create(
  file=open("학습시킬jsonl.jsonl", "rb"),
  purpose="fine-tune"
)

# 방금 업로드한 파일을 이용해 파인튜닝 학습을 시작, model="gpt-3.5-turbo"는 기반 모델로 GPT-3.5-turbo를 사용하겠다는 뜻
client.fine_tuning.jobs.create(
  training_file=file.id,
  model="gpt-3.5-turbo"
)
```
이후 코드를 실행시킨 후

![image](https://github.com/user-attachments/assets/9dcb4e7f-3ee7-4637-a5a7-fcfebb20a376)

파인튜닝 탭에서 파인튜닝이 완료될 때까지 기다리고, Output model명을 복사해서 사용한다.

```
from flask import Flask, request, jsonify
from pymongo import MongoClient
from openai import OpenAI
from dotenv import load_dotenv
import os

# .env 파일 로드
load_dotenv()

# 환경변수에서 값 읽기
openai_api_key = os.environ.get("OPENAI_API_KEY")
mongo_uri = os.environ.get("MONGODB_URI")

client = OpenAI(
    api_key = openai_api_key
)

app = Flask(__name__)

# MongoDB 연결
mongo_client = MongoClient(mongo_uri)
db = mongo_client['SpringDatabaseApi']
collection = db['Api']

@app.route('/medicine/summary', methods=['POST'])
def summarize_medicine():
    data = request.get_json()
    query = data.get("query", "")

    if not query:
        return jsonify({"error": "query 파라미터가 필요합니다."}), 400

    # 1. 제품명 또는 효능에서 검색
    result = collection.find_one({
        "$or": [
            {"itemName": {"$regex": query, "$options": "i"}},
            {"efcyQesitm": {"$regex": query, "$options": "i"}}
        ]
    })

    if not result:
        return jsonify({"error": "관련 약을 찾을 수 없습니다."}), 404

    # 2. 필요한 정보 추출
    item_name = result.get("itemName", "")
    effect = result.get("efcyQesitm", "")
    usage = result.get("useMethodQesitm", "")
    caution = result.get("atpnQesitm", "")

    context_text = f"""
    효능: {effect}
    복용법: {usage}
    주의사항: {caution}
    """

    # 3. OpenAI에 요약 요청
    response =  client.chat.completions.create(
        model="ft:gpt-3.5-turbo-0125:personal::BFDFfQvI",
        messages=[
            {"role": "system", "content": "약사처럼 친절하고 이해하기 쉽게 대답해줘."},
            {"role": "user", "content": f"{context_text}"}
        ]
    )

    summary = response.choices[0].message.content
    return jsonify({
        "itemName": item_name,
        "summary": summary
    })


if __name__ == '__main__':
    app.run('0.0.0.0', port=5000, debug=False)
```
![image](https://github.com/user-attachments/assets/340aba06-84dd-4abb-b3c4-1422eb660d4f)

예시 코드
