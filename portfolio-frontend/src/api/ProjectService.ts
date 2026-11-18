import axios from "axios";

const API_URL = "http://localhost:8080/projects";

export async function getProjects(page = 0, size = 10) {
    return await axios.get(`${API_URL}?page=${page}&size=${size}`)
}

export async function getProject(id: string) {
    return await axios.get(`${API_URL}/${id}`)
}

export async function setProjectActive(id: string, isActive: boolean) {
    return await axios.get(`${API_URL}/${id}/active?active=${isActive}`)
}
