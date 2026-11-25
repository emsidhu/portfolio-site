import { useEffect, useRef, useState } from "react";
import Project from "./Project";
import SectionHeader from "./SectionHeader";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import { PAGE_SIZE } from "../constants";
import { activeProjectsQueryOptions } from "../api/queries";

function ProjectsList() {
  const [currentPage, setCurrentPage] = useState(0);
  const projectsSectionRef = useRef<HTMLDivElement>(null);
  const scrollToBottomAfterLoad = useRef(false);
  const queryClient = useQueryClient();
  const firstPage = useQuery(activeProjectsQueryOptions(0, PAGE_SIZE));
  const totalPages = firstPage.data?.page.totalPages ?? 0;

  const {
    data: projectPage,
    error,
    isPending,
    isError,
  } = useQuery(activeProjectsQueryOptions(currentPage, PAGE_SIZE));

  useEffect(() => {
    if (!firstPage.data) return;

    for (let page = 1; page < firstPage.data.page.totalPages; page += 1) {
      void queryClient.query(activeProjectsQueryOptions(page, PAGE_SIZE));
    }
  }, [firstPage.data, queryClient]);

  useEffect(() => {
    if (!scrollToBottomAfterLoad.current || isPending) return;
    if (isError) {
      scrollToBottomAfterLoad.current = false;
      return;
    }
    if (!projectPage) return;

    projectsSectionRef.current?.scrollIntoView({ behavior: "smooth", block: "end" });
    scrollToBottomAfterLoad.current = false;
  }, [isError, isPending, projectPage]);

  return (
    <div ref={projectsSectionRef} className="container mx-auto mt-8 text-gray-900">
      <div className="grid grid-cols-[1fr_auto_1fr] items-center">
        <div />
        <div className="flex justify-center">
          <SectionHeader text="All Projects" />
        </div>
        {/* For when I have more stuff in the list :) */}
        {/* <div className="flex items-center justify-end">
          <label htmlFor="technologies">Filter: </label>
          <select
            className="ml-1.5 p-0.5 text-sm text-center bg-neutral-secondary-medium border border-default-medium text-heading rounded-full focus:ring-brand focus:border-brand shadow-xs placeholder:text-body"
            name="technologies"
            id="technologies"
            defaultValue=""
          >
            <option value="">--Technology--</option>
            <option value="React">React</option>
            <option value="Java">Java</option>
          </select>
        </div> */}
      </div>
      <div className="container mb-10">
        {isPending ? (
          <div>Loading Projects...</div>
        ) : isError ? (
          <div>Error: {error.message}</div>
        ) : (
          projectPage?.content.map((project) => <Project key={project.id} project={project} />)
        )}
      </div>
      {totalPages > 1 && (
        <nav className="mb-4 flex items-center justify-center gap-4" aria-label="Project pages">
          <button
            type="button"
            className="disabled:opacity-50"
            onClick={() => {
              if (currentPage === 0) return;
              scrollToBottomAfterLoad.current = true;
              setCurrentPage((page) => page - 1);
            }}
            disabled={currentPage === 0}
          >
            <i className="bi bi-chevron-left" />
          </button>
          <span aria-live="polite">
            Page {currentPage + 1} of {totalPages}
          </span>
          <button
            type="button"
            className="disabled:opacity-50"
            onClick={() => setCurrentPage((page) => Math.min(page + 1, totalPages - 1))}
            disabled={currentPage === totalPages - 1}
          >
            <i className="bi bi-chevron-right" />
          </button>
        </nav>
      )}
    </div>
  );
}
export default ProjectsList;
