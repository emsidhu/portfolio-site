function SectionHeader({ text }: { text: string }) {
  return (
    <div className="underline decoration-orange-500 text-center text-4xl w-full md:text-5xl lg:text-6xl py-10 md:pt-0 font-extrabold">
      {text}
    </div>
  );
}

export default SectionHeader;
