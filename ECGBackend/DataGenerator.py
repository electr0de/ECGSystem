from includes import *
import PrintReport as pr
import GetData as gd

port = 12345


def convertToBinaryData():
    # Convert digital data to binary format
    with open('test.pdf', 'rb') as file:
        binaryData = file.read()
    return binaryData




s = socket.socket()
s.bind(('', 4321))
s.listen(1)

while True:
    c,addr = s.accept()
    data = c.recv(1024).decode("utf-8") 
    details = json.loads(data)
    print("got details")
    mydb = mysql.connector.connect(
        host="localhost",
        user="root",
        passwd="",
        database="ecg"
    )
    mycursor = mydb.cursor()
    c.send('ok\n'.encode('ascii'))
    print("sent ok")
    df = gd.getData()


    for index, row in df.iterrows():

        c.recv(1024)
        print(row["ECG"])
        c.send((str(row['ECG'])+'\n').encode('ascii'))

        time.sleep(1/100)

    c.send('done\n'.encode('ascii'))
    bio = nk.ecg_process(ecg=df["ECG"], rsp=None, sampling_rate=100)
    # details = {
    #     'patient_name': 'TestGuy',
    #     'patient_age': 36,
    #     'patient_sex': 'male',
    #     'doctors_name': 'test_doctor',
    #     'technician_name': 'test_technician'
    # }
    pr.printReport(details, bio)
    raw_open = open("bio_100Hz.csv",'rt')
    raw_data = raw_open.read()
    file = convertToBinaryData()
    insert_blob_tuple = (details["id"], details["age"], details["doctor_name"],details["tech_name"],raw_data,file,0,0)

    sql_insert_blob_query = "INSERT INTO report (patient_id, patient_age, doctor_name, tech_name,raw_data,report,verfied_by_tech,verfied_by_doctor) VALUES (%s,%s,%s,%s,%s,%s,%s,%s)"
    mycursor.execute(sql_insert_blob_query, insert_blob_tuple)
    mydb.commit()






