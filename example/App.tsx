import { Button, StyleSheet, Text, View } from "react-native";

import * as ExpoKotlinAudio from "expo-kotlin-audio";
import { useEffect } from "react";

export default function App() {
  return (
    <View style={styles.container}>
      <Text>{ExpoKotlinAudio.hello()}</Text>
      <Button title="Add" onPress={() => ExpoKotlinAudio.add()} />
      <Button title="Play" onPress={() => ExpoKotlinAudio.play()} />
      <Button title="Pause" onPress={() => ExpoKotlinAudio.pause()} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    alignItems: "center",
    justifyContent: "center",
  },
});
