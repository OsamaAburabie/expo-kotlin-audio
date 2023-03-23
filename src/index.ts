import ExpoKotlinAudioModule from "./ExpoKotlinAudioModule";

export function hello(): string {
  return ExpoKotlinAudioModule.hello();
}

export function init(): void {
  ExpoKotlinAudioModule.init();
}

export function add(): void {
  ExpoKotlinAudioModule.add();
}

export function pause(): void {
  ExpoKotlinAudioModule.pause();
}

export function play(): void {
  ExpoKotlinAudioModule.play();
}
