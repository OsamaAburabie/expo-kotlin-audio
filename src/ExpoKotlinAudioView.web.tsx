import * as React from 'react';

import { ExpoKotlinAudioViewProps } from './ExpoKotlinAudio.types';

export default function ExpoKotlinAudioView(props: ExpoKotlinAudioViewProps) {
  return (
    <div>
      <span>{props.name}</span>
    </div>
  );
}
