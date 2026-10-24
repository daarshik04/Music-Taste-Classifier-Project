import { useCallback, useEffect, useState } from "react";
import {spotifyApi} from "./api/client";
import "./index.css";

export default function App(){
  const [connected, setConnected] = useState(false);
  const [tracks, setTracks] = useState<unknown>(null);
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);
  const [importing, setImporting] = useState(false);

  const refereshSession = useCallback(async ()=>{
    try{
      const session = await spotifyApi.getSession();

      console.log("Session Response:", session);
      setConnected(session.hasAccessToken==true);
    }catch(error){
      console.error("Session check failed:", error);
      setConnected(false);
    }
  }, []);

  const loadTracks = async () =>{
    setLoading(true);
    setMessage("");

    try{
      const result = await spotifyApi.getTracks();
      setTracks(result);
    }catch(error){
      setMessage(
        error instanceof Error ? error.message : "Could not load tracks.",
      );
    }finally {
      setLoading(false);
    }
  };

  const importLibrary = async ()=>{
    setImporting(true);
    setMessage("");

    try{
      const result = await spotifyApi.importLibrary();
      setMessage(String(result));
      await loadTracks();
      await refereshSession();
    }catch(error){
      setMessage(
        error instanceof Error ? error.message: "Import Failed.",
      );
    }finally{
      setImporting(false);
    }
  };

  useEffect(()=>{
    const params = new URLSearchParams(window.location.search);
    const status = params.get("spotify");

    if (status == "connected"){
      setMessage("Spotify connected successfully !");
      window.history.replaceState({}, "","/");
    }else if (status === "error"){
      setMessage("Spotify authorization failed.");
      window.history.replaceState({}, "", "/");
    }

    void refereshSession();
  }, [refereshSession]);

  return (
    <main className="app">
      <aside className="sidebar">
        <div className="brand">Know Your Music Taste</div>
        <p className="nav active">Overview</p>
        <p className="nav">My Library</p>
        <p className="nav disabled">Classification . V1</p>
        <p className="nav disabled">Recommendations . Future</p>
        <p className="sidebar-footer">Spotify Classifier . V0</p>
      </aside>

      <section className="content">
        <header className="topbar">
          <div>
            <p className="eyebrow">YOUR MUSIC WORKSPACE</p>
            <h1>Music Library</h1>
            <p className="subtitle">
              Import your Spotify collection and prepare it for classification.
            </p>
          </div>

          <span className={`status ${connected ? "online": ""}`}>
            <span className="dot" />
            {connected ? "Spotify Connected" : "Not connected"}
          </span>
        </header>

        <section className="actions">
          <article className="panel">
            <div className="icon">♫</div>
            <h2>Connect Spotify</h2>
            <p>Authorize access to your saved music.</p>
            <button onClick={()=>{
              window.location.assign(
                `${import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080"}/api/spotify/login`,
              );
            }}
            >Connect account</button>
          </article>

          <article className="panel">
            <div className="icon">↻</div>
            <h2>Import Library</h2>
            <p>Save Spotify tracks to DB to know your taste</p>
            <button 
              disabled ={!connected || importing}
              onClick={()=> void importLibrary()}>
                {importing ? "Importing..." : "Import tracks"}
              </button>
          </article>

          <article className="panel">
            <div className="secondary">≡</div>
            <h2>Browse Tracks</h2>
            <p>Preview tracks returned by the Spotify API.</p>
            <button 
               className="secondary"
               disabled={!connected || loading}
               onClick={()=> void loadTracks()}>
                {loading ? "Loading..." : "Load Tracks"}
               </button>
          </article>
        </section>

        {message &&(
          <div className="notice" role="status">
            {message}
          </div>
        )}

        <section className="panel library">
          <div className="section-heading">
            <div>
              <p className="eyebrow">YOUR COLLECTION</p>
              <h2>Track response</h2>
            </div>
            <button className="secondary"
                    disabled={!connected || loading}
                    onClick={()=> void loadTracks()}>Refresh</button>
          </div>

          {tracks ===null?(
            <div className="empty">
              <span className="empty-icon">♫</span>
              <h3>Your music will appear here</h3>
              <p>Connect Spotify, then load your tracks.</p>
            </div>
          ):(
            <pre className="response">
              {JSON.stringify(tracks, null,2)}
            </pre>
          )}
        </section>
      </section>
    </main>
  );
}