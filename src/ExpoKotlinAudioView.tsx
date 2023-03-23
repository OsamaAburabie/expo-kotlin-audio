import { requireNativeViewManager } from 'expo-modules-core';
import * as React from 'react';

import { ExpoKotlinAudioViewProps } from './ExpoKotlinAudio.types';

const NativeView: React.ComponentType<ExpoKotlinAudioViewProps> =
  requireNativeViewManager('ExpoKotlinAudio');

export default function ExpoKotlinAudioView(props: ExpoKotlinAudioViewProps) {
  return <NativeView {...props} />;
}
