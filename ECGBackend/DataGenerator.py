from includes import *
import PrintReport as pr
import GetData as gd


# converts pdf to binary data
def convertToBinaryData():
    # Convert digital data to binary format
    with open('test.pdf', 'rb') as file:
        binaryData = file.read()
    return binaryData


# gets a socket object to open connection
def getSocket(port):
    s = socket.socket()
    s.bind(('', port))
    s.listen(1)
    return s


# returns db object
def returnDbCursor():
    mydb = mysql.connector.connect(
        host="localhost",
        user="root",
        passwd="",
        database="ecg"
    )

    return mydb


s = getSocket(4321)
while True:
    c, addr = s.accept()
    data = c.recv(1024).decode("utf-8")
    details = json.loads(data)
    print("got details")

    c.send('ok\n'.encode('ascii'))
    print("sent ok")
    df = gd.getData()

    for index, row in df.iterrows():
        c.recv(1024)
        print(row["ECG"])
        c.send((str(row['ECG']) + '\n').encode('ascii'))

        time.sleep(1 / 100)

    c.send('done\n'.encode('ascii'))

    # process ECG data
    bio = nk.ecg_process(ecg=df["ECG"], rsp=None, sampling_rate=100)

    pr.printReport(details, bio)
    raw_open = open("bio_100Hz.csv", 'rt')
    raw_data = raw_open.read()
    file = convertToBinaryData()
    insert_blob_tuple = (
    details["id"], details["age"], details["doctor_name"], details["tech_name"], raw_data, file, 0, 0)
    mydb = returnDbCursor()
    mycursor = mydb.cursor()

    # insert report into database
    sql_insert_blob_query = "INSERT INTO report (patient_id, patient_age, doctor_name, tech_name,raw_data,report,verfied_by_tech,verfied_by_doctor) VALUES (%s,%s,%s,%s,%s,%s,%s,%s)"
    mycursor.execute(sql_insert_blob_query, insert_blob_tuple)
    mydb.commit()
