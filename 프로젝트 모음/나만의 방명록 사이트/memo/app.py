from flask import Flask, render_template, jsonify, request
app = Flask(__name__)

import requests
import os # 파일 경로 처리와 파일 삭제 등을 위한 모듈
from bs4 import BeautifulSoup
from werkzeug.utils import secure_filename # 파일 이름에 사용할 수 없는 특수 문자를 제거하여 안전한 파일 이름을 생성하는 유틸리티 함수

UPLOAD_FOLDER = "./static/uploads"  # 이미지를 저장할 경로 변수
UPLOAD_FOLDER2 = "./static/uploads2"
ALLOWED_EXTENSIONS = {'png', 'jpg', 'jpeg', 'gif', 'webp'} # 로드할 수 있는 이미지 파일 확장자를 설정
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER  # 이미지를 저장할 경로 설정
app.config['UPLOAD_FOLDER2'] = UPLOAD_FOLDER2

from pymongo import MongoClient
uri = "mongodb+srv://skcy151515:IyuTp1jwPnkfLXXl@cluster0.es5up.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0&tlsAllowInvalidCertificates=true"
client = MongoClient(uri, 27017)  # MongoDB는 27017 포트로 돌아갑니다.
db = client.dbmemo # dbmemo라는 데이터베이스 사용

## HTML을 주는 부분
@app.route('/')
def home():
   return render_template('index.html')

## HTML을 주는 부분
@app.route('/imageMemo')
def home2():
   return render_template('imageMemo.html')

## HTML을 주는 부분
@app.route('/imageMemoPrivate')
def home3():
   return render_template('imageMemo2.html')

# 업로드 파일 확장자 확인 함수
# '.' in filename: 이 조건은 파일 이름에 점(.)이 있는지를 확인한다. 파일 이름에 점이 없으면 확장자가 없으므로, 이 조건이 False로 반환된다.
# filename.rsplit('.', 1)[1].lower(): filename.rsplit('.', 1)는 파일 이름을 점(.)을 기준으로 오른쪽부터 한 번만 분리하고 소문자로 변경
# 예를 들어 image.img rsplit('.', 1)[1]를 통해 분리[image, img]하고, 두 번째 요소 [img]를 가져와 확장자를 검사함.
def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

## API 역할을 하는 부분
@app.route('/image', methods=['GET'])
def read_images():
    # 1. MongoDB에서 _id 값을 제외한 모든 데이터 조회해오기 (Read)
    result = list(db.imageMemo.find({}, {'_id': 0}))
    # 2. image라는 키 값으로 image 정보 보내주기
    return jsonify({'result': 'success', 'image': result})

@app.route('/image', methods=['POST'])
def post_image():
    if 'file_give' not in request.files:
        return jsonify({'result': 'fail', 'error': '파일이 누락되었습니다.'})

    # 파일과 코멘트를 받음
    file = request.files['file_give']
    comment_receive = request.form['comment_give']

    if file and allowed_file(file.filename):
        # secure_filename(): 특수 문자 제거
        filename = secure_filename(file.filename)
        file_path = os.path.join(app.config['UPLOAD_FOLDER'], filename) # 경로 지정 ex) ./static/uploads/image.jpg

        # 파일 이름이 이미 존재하는지 확인
        while os.path.exists(file_path):
            # 파일이 존재하면 이름 뒤에 's'를 추가하여 새 파일명 생성
            name, extension = os.path.splitext(filename)  # splitext()는 파일 경로 또는 파일 이름을 확장자와 그 외 부분으로 나누는 함수 ex) name = image, extension =.img
            filename = f"{name}s{extension}"  # 이름 뒤에 's' 추가
            file_path = os.path.join(app.config['UPLOAD_FOLDER'], filename)  # 새로운 경로 설정
        file.save(file_path)  # 서버에 파일 저장

        # MongoDB에 데이터 삽입
        image = {
            'image': f"/static/uploads/{filename}",  # 저장된 경로, f""는 f-string(포맷 문자열 리터럴)
            'comment': comment_receive,
        }
        db.imageMemo.insert_one(image)

        return jsonify({'result': 'success'})
    else:
        return jsonify({'result': 'fail', 'error': '허용되지 않는 파일 형식입니다.'})

@app.route('/image', methods=['DELETE'])
def delete_image():
    url_receive = request.form['url_give']  # 클라이언트에서 받은 URL
    file_path = os.path.join(app.config['UPLOAD_FOLDER'], url_receive.split('/')[-1])  # 파일 경로 추출, [1]: split('/')로 나눈 리스트에서 마지막 요소

    db.imageMemo.delete_one({'image': url_receive})  # MongoDB에서 해당 URL로 문서 삭제

    # 서버에서 파일 삭제
    if os.path.exists(file_path):
        os.remove(file_path)
        print(f"파일 {file_path} 삭제됨.")
    else:
        print(f"파일 {file_path}이(가) 존재하지 않음.")

    return jsonify({'result': 'success'})

@app.route('/image/all', methods=['DELETE'])
def delete_all_articles():
    # 모든 이미지 삭제
    db.imageMemo.delete_many({})  # 모든 문서 삭제
    
    # 업로드된 파일들도 삭제
    upload_folder = app.config['UPLOAD_FOLDER']
    # os.listdir(upload_folder)는 지정된 폴더(upload_folder, ./static/uploads) 내의 모든 파일과 디렉토리 이름을 리스트로 반환한다.
    for filename in os.listdir(upload_folder):
        file_path = os.path.join(upload_folder, filename)
        if os.path.isfile(file_path):
            os.remove(file_path)  # 파일 삭제
    
    return jsonify({'result': 'success'})

# private 전용
## API 역할을 하는 부분
@app.route('/image2', methods=['GET'])
def read_images2():
    # 1. MongoDB에서 _id 값을 제외한 모든 데이터 조회해오기 (Read)
    result = list(db.imageMemo2.find({}, {'_id': 0}))
    # 2. image라는 키 값으로 image 정보 보내주기
    return jsonify({'result': 'success', 'image': result})

@app.route('/image2', methods=['POST'])
def post_image2():
    if 'file_give' not in request.files:
        return jsonify({'result': 'fail', 'error': '파일이 누락되었습니다.'})

    # 파일과 코멘트를 받음
    file = request.files['file_give']
    comment_receive = request.form['comment_give']

    if file and allowed_file(file.filename):
        # secure_filename(): 특수 문자 제거
        filename = secure_filename(file.filename)
        file_path = os.path.join(app.config['UPLOAD_FOLDER2'], filename) # 경로 지정 ex) ./static/uploads/image.jpg

        # 파일 이름이 이미 존재하는지 확인
        while os.path.exists(file_path):
            # 파일이 존재하면 이름 뒤에 's'를 추가하여 새 파일명 생성
            name, extension = os.path.splitext(filename)  # splitext()는 파일 경로 또는 파일 이름을 확장자와 그 외 부분으로 나누는 함수 ex) name = image, extension =.img
            filename = f"{name}s{extension}"  # 이름 뒤에 's' 추가
            file_path = os.path.join(app.config['UPLOAD_FOLDER2'], filename)  # 새로운 경로 설정
        file.save(file_path)  # 서버에 파일 저장

        # MongoDB에 데이터 삽입
        image = {
            'image': f"/static/uploads2/{filename}",  # 저장된 경로, f""는 f-string(포맷 문자열 리터럴)
            'comment': comment_receive,
        }
        db.imageMemo2.insert_one(image)

        return jsonify({'result': 'success'})
    else:
        return jsonify({'result': 'fail', 'error': '허용되지 않는 파일 형식입니다.'})

@app.route('/image2', methods=['DELETE'])
def delete_image2():
    url_receive = request.form['url_give']  # 클라이언트에서 받은 URL
    file_path = os.path.join(app.config['UPLOAD_FOLDER2'], url_receive.split('/')[-1])  # 파일 경로 추출, [1]: split('/')로 나눈 리스트에서 마지막 요소

    db.imageMemo2.delete_one({'image': url_receive})  # MongoDB에서 해당 URL로 문서 삭제

    # 서버에서 파일 삭제
    if os.path.exists(file_path):
        os.remove(file_path)
        print(f"파일 {file_path} 삭제됨.")
    else:
        print(f"파일 {file_path}이(가) 존재하지 않음.")

    return jsonify({'result': 'success'})

@app.route('/image2/all', methods=['DELETE'])
def delete_all_articles2():
    # 모든 이미지 삭제
    db.imageMemo2.delete_many({})  # 모든 문서 삭제
    
    # 업로드된 파일들도 삭제
    upload_folder = app.config['UPLOAD_FOLDER2']
    # os.listdir(upload_folder)는 지정된 폴더(upload_folder, ./static/uploads) 내의 모든 파일과 디렉토리 이름을 리스트로 반환한다.
    for filename in os.listdir(upload_folder):
        file_path = os.path.join(upload_folder, filename)
        if os.path.isfile(file_path):
            os.remove(file_path)  # 파일 삭제
    
    return jsonify({'result': 'success'})

if __name__ == '__main__':
   app.run('0.0.0.0',port=5000,debug=True)