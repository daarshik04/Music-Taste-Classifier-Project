const API_BASE_URL =import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080";

async function request<T> (
    path: string,
    options: RequestInit = {},
): Promise<T>{

    const response =await fetch(`${API_BASE_URL}${path}`,{
        ...options,
        credentials: "include",
        headers:{
            Accept: "application/json, text/plain",
            ...options.headers,
        },
    });

    const body = await response.text();

    if(!response.ok){
        throw new Error(body || `Request Failed: ${response.status}`);
    }

    if(!body) return undefined as T;

    try{
        return JSON.parse(body) as T;
    }catch{
        return body as T;
    }
}

export const spotifyApi = {
    getSession: ()=>
        request<{ hasAccessToken: boolean}>(
            "/api/spotify/debug/session",{
                credentials : "include"
            }
        ),

    getTracks: ()=>
        request<unknown>("/api/spotify/tracks"),    

    importLibrary: ()=>
        request<string>("/api/spotify/import",{
            method:"POST",
        }),
};