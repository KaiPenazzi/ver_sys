'use client'
import {invoke} from "@tauri-apps/api/tauri"

export default function Home() {
    const test = async () => {
        await invoke<>('start_game', {}).catch(console.error)
    }

    const nothing = () => {
        invoke<>('do_nothing', {})
    }

    return (
        <div>
            <h1>Hallo Welt!!!</h1>
            <button onClick={test}>start game</button>
            <button onClick={nothing}>do nothing</button>
        </div>
    );
}
