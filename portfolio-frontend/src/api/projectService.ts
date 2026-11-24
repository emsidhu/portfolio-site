import axios from "axios";

const API_URL = "http://localhost:8080/projects";

export interface Project {
  id: number;
  name: string | null
  title: string | null;
  description: string | null;
  featuredDescription: string | null;
  latestCommitDate: string | null;
  technologies: string[];
  photoUrl: string | null;
  isActive: boolean;
  isFeatured: boolean;
  isCurrent: boolean;
}

export interface ProjectPage {
  content: Project[];
}

export async function getActiveProjects(page = 0, size = 10) {
  const response = await axios.get<ProjectPage>(`${API_URL}/active?page=${page}&size=${size}`);
  return response.data.content;
}

export async function getFeaturedProjects() {
  const response = await axios.get<Project[]>(`${API_URL}/featured`);
  return response.data
}

export async function getCurrentProject() {
  const response = await axios.get<Project>(`${API_URL}/current`);
  return response.data
}

export async function getProject(id: string) {
  const response = await axios.get<Project>(`${API_URL}/${id}`);
  return response.data
}

export async function setProjectFeatured(id: string, isFeatured: boolean) {
  return await axios.patch<Project>(`${API_URL}/${id}/featured?isFeatured=${isFeatured}`);
}

export async function setProjectCurrent(id: string) {
  return await axios.patch<Project>(`${API_URL}/${id}/current`);
}