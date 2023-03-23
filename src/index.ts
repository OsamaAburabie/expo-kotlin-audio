import { NativeModulesProxy, EventEmitter, Subscription } from 'expo-modules-core';

// Import the native module. On web, it will be resolved to ExpoKotlinAudio.web.ts
// and on native platforms to ExpoKotlinAudio.ts
import ExpoKotlinAudioModule from './ExpoKotlinAudioModule';
import ExpoKotlinAudioView from './ExpoKotlinAudioView';
import { ChangeEventPayload, ExpoKotlinAudioViewProps } from './ExpoKotlinAudio.types';

// Get the native constant value.
export const PI = ExpoKotlinAudioModule.PI;

export function hello(): string {
  return ExpoKotlinAudioModule.hello();
}

export async function setValueAsync(value: string) {
  return await ExpoKotlinAudioModule.setValueAsync(value);
}

const emitter = new EventEmitter(ExpoKotlinAudioModule ?? NativeModulesProxy.ExpoKotlinAudio);

export function addChangeListener(listener: (event: ChangeEventPayload) => void): Subscription {
  return emitter.addListener<ChangeEventPayload>('onChange', listener);
}

export { ExpoKotlinAudioView, ExpoKotlinAudioViewProps, ChangeEventPayload };
