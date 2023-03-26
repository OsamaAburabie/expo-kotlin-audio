import ExpoKotlinAudioModule from "./ExpoKotlinAudioModule";
import { EventEmitter, Subscription } from "expo-modules-core";
const emitter = new EventEmitter(ExpoKotlinAudioModule);

export type StateChangeEvent = {
  state: AudioPlayerState;
};
export type TrackChangeEvent = {
  track: Track;
};

export type TrackIndexChangeEvent = {
  index: number;
};

export type QueueChangeEvent = {
  queue: Track[];
};

export enum AudioPlayerState {
  LOADING = "LOADING",
  READY = "READY",
  BUFFERING = "BUFFERING",
  PAUSED = "PAUSED",
  STOPPED = "STOPPED",
  PLAYING = "PLAYING",
  IDLE = "IDLE",
  ENDED = "ENDED",
  ERROR = "ERROR",
}

export type Track = {
  uri: string;
  title: string;
  artist: string;
  artwork?: string;
  duration?: number;
};

export function addStateListener(
  listener: (event: StateChangeEvent) => void
): Subscription {
  return emitter.addListener<StateChangeEvent>("onStateChange", listener);
}

export function addTrackListener(
  listener: (event: TrackChangeEvent) => void
): Subscription {
  return emitter.addListener<TrackChangeEvent>("onTrackChange", listener);
}

export function addTrackIndexListener(
  listener: (event: TrackIndexChangeEvent) => void
): Subscription {
  return emitter.addListener<TrackIndexChangeEvent>(
    "onTrackIndexChange",
    listener
  );
}

export function addQueueListener(
  listener: (event: QueueChangeEvent) => void
): Subscription {
  return emitter.addListener<QueueChangeEvent>("onQueueChange", listener);
}

export function hello(): string {
  return ExpoKotlinAudioModule.hello();
}

export function init(): void {
  ExpoKotlinAudioModule.init();
}

export function add(tracks: Track[]): void {
  ExpoKotlinAudioModule.add(tracks);
}

export function pause(): void {
  ExpoKotlinAudioModule.pause();
}

export function play(): void {
  ExpoKotlinAudioModule.play();
}

export function stop(): void {
  ExpoKotlinAudioModule.stop();
}

export function next(): void {
  ExpoKotlinAudioModule.next();
}

export function previous(): void {
  ExpoKotlinAudioModule.previous();
}

export function skipTo(index: number): void {
  ExpoKotlinAudioModule.skipTo(index);
}

export function getDuration(): number {
  return ExpoKotlinAudioModule.getDuration();
}

export function getPosition(): number {
  return ExpoKotlinAudioModule.getPosition();
}

export function getPlaying(): boolean {
  return ExpoKotlinAudioModule.getPlaying();
}

export function getQueue(): Track[] {
  return ExpoKotlinAudioModule.getQueue();
}

export function getCurrentIndex(): number {
  return ExpoKotlinAudioModule.getCurrentIndex();
}

export function getCurrentTrack(): Track {
  return ExpoKotlinAudioModule.getCurrentTrack();
}

export function getState(): AudioPlayerState {
  return ExpoKotlinAudioModule.getState();
}
