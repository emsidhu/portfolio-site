import { Routes, Route } from "react-router-dom";
import "./App.css";
import Home from "./pages/Home";
import ProjectsPage from "./pages/ProjectsPage";
import Navbar from "./components/Navbar";
import ResumePage from "./pages/ResumePage";

function App() {
  return (
    <>
      <Navbar />
      <main>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/projects" element={<ProjectsPage />} />
          <Route path="/resume" element={<ResumePage />} />
        </Routes>
      </main>
    </>
  );
}

export default App;
