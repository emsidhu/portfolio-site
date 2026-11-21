function SectionHeader({ text }: { text: string }) {
  return (
    <div className="text-center mb-4 text-3xl font-semibold">
      <h2 className="underline decoration-orange-500 mx-auto inline">{text}</h2>
    </div>
  );
}

export default SectionHeader;
