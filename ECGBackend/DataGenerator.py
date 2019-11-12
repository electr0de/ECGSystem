import neurokit as nk
import pandas as pd
import numpy as np
import seaborn as sns
import matplotlib.pyplot as plt
from fpdf import FPDF
import socket
import json
import mysql.connector
import time

port = 12345


def convertToBinaryData():
    # Convert digital data to binary format
    with open('test.pdf', 'rb') as file:
        binaryData = file.read()
    return binaryData

def printReport(details,bio):
    nk.z_score(bio["df"][["ECG_Raw", "ECG_Filtered"]])[1000:1500].plot()
    plt.savefig("ecg.png")
    pd.DataFrame(bio["ECG"]["Cardiac_Cycles"]).plot(legend=False)
    plt.savefig("ecg_cardiacCycle.png")
    pd.DataFrame(bio["ECG"]["HRV"]["RR_Intervals"]).plot(legend=False)
    plt.savefig("HRV.png")
    pdf = FPDF(orientation = 'P', unit = 'mm', format='A4')
    pdf.add_page()
    pdf.set_xy(0, 0)
    pdf.set_font('arial', 'B', 12)
    pdf.cell(75, 10, "Report", 0, 1, 'C')
    pdf.cell(60, 10, "Patient Name: %s"% (details["patient_name"]), 0, 0, 'C')
    pdf.cell(60, 10, "Patient Name: %s" % (details["patient_age"]), 0, 1, 'C')
    pdf.cell(60, 10, "Patient Name: %s" % (details["patient_sex"]), 0, 0, 'C')
    pdf.cell(60, 10, "Patient Name: %s" % (details["doctors_name"]), 0, 1, 'C')
    pdf.cell(60, 10, "Patient Name: %s" % (details["technician_name"]), 0, 1, 'C')
    pdf.image('ecg.png', x=None, y=None, w=150, h=0, type='', link='')
    pdf.cell(75, 10, "ECG", 0, 1, 'C')
    pdf.cell(60, 10, "ECG Signal Quality: %f" % (bio["ECG"]["Average_Signal_Quality"]), 0, 1, 'C')
    pdf.image('ecg_cardiacCycle.png', x=None, y=None, w=150, h=0, type='', link='')
    pdf.cell(75, 10, "ECG Heart Beats/Cardiac Cycle", 0, 1, 'C')
    pdf.image('HRV.png', x=None, y=None, w=150, h=0, type='', link='')
    pdf.cell(75, 10, "Heart Rate Variability", 0, 1, 'C')
    pdf.cell(50, 10, "'RMSSD':  %f" % (bio["ECG"]["HRV"]["RMSSD"]), 0, 0, 'C')
    pdf.cell(50, 10, "'meanNN':  %f" % (bio["ECG"]["HRV"]["meanNN"]), 0, 1, 'C')
    pdf.cell(50, 10, "'sdNN':  %f" % (bio["ECG"]["HRV"]["sdNN"]), 0, 0, 'C')
    pdf.cell(50, 10, "'cvNN':  %f" % (bio["ECG"]["HRV"]["cvNN"]), 0, 1, 'C')
    pdf.cell(50, 10, "'CVSD':  %f" % (bio["ECG"]["HRV"]["CVSD"]), 0, 0, 'C')
    pdf.cell(50, 10, "'medianNN':  %f" % (bio["ECG"]["HRV"]["medianNN"]), 0, 1, 'C')
    pdf.cell(50, 10, "'madNN':  %f" % (bio["ECG"]["HRV"]["madNN"]), 0, 0, 'C')
    pdf.cell(50, 10, "'mcvNN':  %f" % (bio["ECG"]["HRV"]["mcvNN"]), 0, 1, 'C')
    pdf.cell(50, 10, "'pNN50':  %f" % (bio["ECG"]["HRV"]["pNN50"]), 0, 0, 'C')
    pdf.cell(50, 10, "'pNN20':  %f" % (bio["ECG"]["HRV"]["pNN20"]), 0, 1, 'C')
    pdf.cell(50, 10, "'Triang':  %f" % (bio["ECG"]["HRV"]["Triang"]), 0, 0, 'C')
    pdf.cell(50, 10, "'Shannon_h':  %f" % (bio["ECG"]["HRV"]["Shannon_h"]), 0, 1, 'C')
    pdf.cell(50, 10, "'ULF':  %f" % (bio["ECG"]["HRV"]["ULF"]), 0, 0, 'C')
    pdf.cell(50, 10, "'VLF':  %f" % (bio["ECG"]["HRV"]["VLF"]), 0, 1, 'C')
    pdf.cell(50, 10, "'LF':  %f" % (bio["ECG"]["HRV"]["LF"]), 0, 0, 'C')
    pdf.cell(50, 10, "'HF':  %f" % (bio["ECG"]["HRV"]["HF"]), 0, 1, 'C')
    pdf.cell(50, 10, "'VHF':  %f" % (bio["ECG"]["HRV"]["VHF"]), 0, 0, 'C')
    pdf.cell(50, 10, "'Total_Power':  %f" % (bio["ECG"]["HRV"]["Total_Power"]), 0, 1, 'C')
    pdf.cell(50, 10, "'LFn':  %f" % (bio["ECG"]["HRV"]["LFn"]), 0, 0, 'C')
    pdf.cell(50, 10, "'HFn':  %f" % (bio["ECG"]["HRV"]["HFn"]), 0, 1, 'C')
    pdf.cell(50, 10, "'HFn':  %f" % (bio["ECG"]["HRV"]["HFn"]), 0, 0, 'C')
    pdf.cell(50, 10, "'LF/HF':  %f" % (bio["ECG"]["HRV"]["LF/HF"]), 0, 1, 'C')
    pdf.cell(50, 10, "'LF/P':  %f" % (bio["ECG"]["HRV"]["LF/P"]), 0, 0, 'C')
    pdf.cell(50, 10, "'HF/P':  %f" % (bio["ECG"]["HRV"]["HF/P"]), 0, 1, 'C')

    pdf.output('test.pdf', 'F')

def getData():
    df = pd.read_csv("bio_100Hz.csv")
    return df
s = socket.socket()
s.bind(('', port))
s.listen(1)

while True:
    c = s.accept()
    data = c.recv(1024).decode("utf-8")
    details = json.loads(data)

    mydb = mysql.connector.connect(
        host="localhost",
        user="yourusername",
        passwd="yourpassword"
    )

    sql = "INSERT INTO patient (name,sex, age) VALUES (%s, %s, %d)"
    value = (details["name"],details["age"],details["sex"])
    mycursor = mydb.cursor()

    mycursor.execute(sql, value)

    mydb.commit()

    df = getData()
    # Process the signals
    for index, row in df.iterrows():
        c.send(row['ECG'])
        time.sleep(1/100)

    c.send("done")
    bio = nk.ecg_process(ecg=df["ECG"], rsp=None, sampling_rate=100)
    # details = {
    #     'patient_name': 'TestGuy',
    #     'patient_age': 36,
    #     'patient_sex': 'male',
    #     'doctors_name': 'test_doctor',
    #     'technician_name': 'test_technician'
    # }
    printReport(details, bio)
    
    file = convertToBinaryData()
    insert_blob_tuple = (emp_id, name, empPicture, file)





