from includes import pd


# reads data from file
def getData():
    df = pd.read_csv("bio_100Hz.csv")
    return df
