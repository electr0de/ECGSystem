from includes import pd
def getData():
    df = pd.read_csv("bio_100Hz.csv")
    return df