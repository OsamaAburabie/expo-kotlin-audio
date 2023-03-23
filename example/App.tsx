import { StyleSheet, Text, View } from 'react-native';

import * as ExpoKotlinAudio from 'expo-kotlin-audio';

export default function App() {
  return (
    <View style={styles.container}>
      <Text>{ExpoKotlinAudio.hello()}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
