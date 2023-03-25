import { Dimensions, FlatList, Pressable, Text, View } from "react-native";

import * as ExpoKotlinAudio from "expo-kotlin-audio";
import React, { useEffect, useState } from "react";

const { height, width } = Dimensions.get("window");

export default function App() {
  const [currentTrack, setCurrentTrack] = useState({
    index: 0,
    withScroll: false,
  });

  const flatListRef = React.useRef<FlatList>(null);

  useEffect(() => {
    const sub = ExpoKotlinAudio.addQueueListener((state) => {
      // console.log("state", state.queue);
    });

    const indexSub = ExpoKotlinAudio.addTrackIndexListener((index) => {
      if (typeof index.index === "number") {
        setCurrentTrack({
          index: index.index,
          withScroll: true,
        });
      }
    });

    return () => {
      sub.remove();
      indexSub.remove();
    };
  }, []);

  useEffect(() => {
    try {
      ExpoKotlinAudio.skipTo(currentTrack.index);

      if (currentTrack.withScroll) {
        flatListRef.current?.scrollToIndex({
          index: currentTrack.index,
        });
      }
    } catch (e) {
      console.log(e);
    }
  }, [currentTrack.index]);
  return (
    // <View style={styles.container}>
    //   <Button
    //     title="Add"
    //     onPress={() =>
    //       ExpoKotlinAudio.add([
    //         {
    //           artist: "artist",
    //           title: "title",
    //           uri: "https://cdn.pixabay.com/download/audio/2022/08/25/audio_4f3b0a816e.mp3?filename=tuesday-glitch-soft-hip-hop-118327.mp3",
    //           artwork:
    //             "https://images-na.ssl-images-amazon.com/images/I/A18QUHExFgL._SL1500_.jpg",
    //         },
    //         {
    //           artist: "artist2",
    //           title: "title2",
    //           uri: "https://cdn.pixabay.com/download/audio/2022/08/31/audio_419263fc12.mp3?filename=leonell-cassio-the-blackest-bouquet-118766.mp3",
    //           artwork:
    //             "https://upload.wikimedia.org/wikipedia/en/0/0b/DirtyComputer.png",
    //         },
    //       ])
    //     }
    //   />
    //   <Button title="Play" onPress={() => ExpoKotlinAudio.play()} />
    //   <Button title="Pause" onPress={() => ExpoKotlinAudio.pause()} />
    //   <Button title="next" onPress={() => ExpoKotlinAudio.next()} />
    //   <Button title="prev" onPress={() => ExpoKotlinAudio.previous()} />
    //   <Button title="skipTo 0" onPress={() => ExpoKotlinAudio.skipTo(0)} />
    //   <Button title="skipTo 1" onPress={() => ExpoKotlinAudio.skipTo(1)} />
    // </View>

    <View
      style={{
        flex: 1,
      }}
    >
      <FlatList
        ref={flatListRef}
        data={[0, 1]}
        horizontal
        pagingEnabled
        renderItem={({ index }) => (
          <View
            style={{
              width,
              height,
              justifyContent: "center",
              alignItems: "center",
              backgroundColor: "gray",
            }}
          >
            <Pressable
              onPress={() =>
                ExpoKotlinAudio.add([
                  {
                    artist: "artist",
                    title: "title",
                    uri: "https://cdn.pixabay.com/download/audio/2022/08/25/audio_4f3b0a816e.mp3?filename=tuesday-glitch-soft-hip-hop-118327.mp3",
                    artwork:
                      "https://images-na.ssl-images-amazon.com/images/I/A18QUHExFgL._SL1500_.jpg",
                  },
                  {
                    artist: "artist2",
                    title: "title2",
                    uri: "https://cdn.pixabay.com/download/audio/2022/08/31/audio_419263fc12.mp3?filename=leonell-cassio-the-blackest-bouquet-118766.mp3",
                    artwork:
                      "https://upload.wikimedia.org/wikipedia/en/0/0b/DirtyComputer.png",
                  },
                ])
              }
              style={{
                height: 200,
                width: 200,
                borderRadius: 20,
                backgroundColor: "red",
                justifyContent: "center",
                alignItems: "center",
              }}
            >
              <Text
                style={{
                  color: "white",
                  fontSize: 30,
                }}
              >
                {index}
              </Text>
            </Pressable>

            <Pressable
              onPress={() => {
                console.log(ExpoKotlinAudio.getQueue());
              }}
              style={{
                height: 200,
                width: 200,
                borderRadius: 20,
                backgroundColor: "red",
              }}
            />
          </View>
        )}
        onMomentumScrollEnd={(e) => {
          const index = Math.round(e.nativeEvent.contentOffset.x / width);

          setCurrentTrack({
            index,
            withScroll: false,
          });
        }}
      />
    </View>
  );
}
